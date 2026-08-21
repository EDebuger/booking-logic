// ==================== GLOBAL VARIABLES ====================
let allRestaurants = [];
let filteredRestaurants = [];
let currentCarouselIndex = 0;
let carouselInterval;
const CAROUSEL_INTERVAL = 5000; // 5 seconds

// ==================== PAGE LOAD ====================
document.addEventListener('DOMContentLoaded', async () => {
    // Check if user is logged in
    if (!isLoggedin()) {
        window.location.href = '/auth/login';
        return;
    }

    // Load all restaurants
    await loadAllRestaurants();

    // Update user info in header
    updateUserInfo();

    // Populate company filter dropdown
    populateCompanyFilter();

    // Setup event listeners for filters
    setupFilterListeners();

    // Initialize carousel with available restaurants
    await initializeCarousel();
});

// ==================== LOAD RESTAURANTS ====================
async function loadAllRestaurants() {
    try {
        const response = await apiFetch('/restaurants/getAll');
        if (!response.ok) {
            throw new Error('Failed to load restaurants');
        }
        allRestaurants = await response.json();
        filteredRestaurants = [...allRestaurants];
        displayRestaurants(filteredRestaurants);
    } catch (error) {
        console.error('Error loading restaurants:', error);
        showAlert('Failed to load restaurants. Please try again.');
    }
}

// ==================== USER INFO ====================
function updateUserInfo() {
    const token = localStorage.getItem('token');
    if (token) {
        try {
            // Decode JWT to extract username
            const decoded = decodeJWT(token);
            const username = decoded.sub || decoded.username || 'User';
            document.getElementById('userName').textContent = username;
        } catch (error) {
            console.error('Error decoding token:', error);
            document.getElementById('userName').textContent = 'User';
        }
    } else {
        document.getElementById('userName').textContent = 'Guest';
    }
}

/**
 * Simple JWT decoder (handles basic claims)
 */
function decodeJWT(token) {
    try {
        const parts = token.split('.');
        if (parts.length !== 3) throw new Error('Invalid token');

        const payload = parts[1];
        const decoded = JSON.parse(atob(payload));
        return decoded;
    } catch (error) {
        console.error('JWT decode error:', error);
        return {};
    }
}

// ==================== FILTER SETUP ====================
function setupFilterListeners() {
    document.getElementById('searchBar').addEventListener('input', applyFilters);
    document.getElementById('sortBySelect').addEventListener('change', applyFilters);
    document.getElementById('serviceTypeFilter').addEventListener('change', applyFilters);
    document.getElementById('companyFilter').addEventListener('change', applyFilters);
    document.getElementById('availableCheckbox').addEventListener('change', applyFilters);
}

/**
 * Populate company filter dropdown from loaded restaurants
 */
function populateCompanyFilter() {
    const companySet = new Set();
    allRestaurants.forEach(restaurant => {
        if (restaurant.subOf && restaurant.subOf > 0) {
            companySet.add(restaurant.subOf);
        }
    });

    const companyFilter = document.getElementById('companyFilter');
    // Clear existing options except the first one
    while (companyFilter.options.length > 1) {
        companyFilter.remove(1);
    }

    companySet.forEach(companyId => {
        const option = document.createElement('option');
        option.value = companyId;
        option.textContent = `Company ${companyId}`; // TODO: Replace with actual company names if available
        companyFilter.appendChild(option);
    });
}

// ==================== FILTER & SORT LOGIC ====================
async function applyFilters() {
    let filtered = [...allRestaurants];

    // 1. Search filter (by name)
    const searchTerm = document.getElementById('searchBar').value.toLowerCase().trim();
    if (searchTerm) {
        filtered = filtered.filter(r =>
            r.name.toLowerCase().includes(searchTerm)
        );
    }

    // 2. Service type filter
    const serviceType = document.getElementById('serviceTypeFilter').value;
    if (serviceType) {
        filtered = filtered.filter(r => r.serviceType === serviceType);
    }

    // 3. Company filter
    const company = document.getElementById('companyFilter').value;
    if (company && company !== '0') {
        filtered = filtered.filter(r => r.subOf == company);
    }

    // 4. Available checkbox (check for available tables)
    const availableOnly = document.getElementById('availableCheckbox').checked;
    if (availableOnly) {
        filtered = await filterByAvailability(filtered);
    }

    // 5. Sorting
    const sortBy = document.getElementById('sortBySelect').value;
    filtered = applySorting(filtered, sortBy);

    // Update results
    filteredRestaurants = filtered;
    displayRestaurants(filteredRestaurants);
}

/**
 * Filter restaurants that have available tables
 */
async function filterByAvailability(restaurants) {
    const availableRestaurants = [];

    for (const restaurant of restaurants) {
        try {
            const response = await apiFetch(`/restaurants/${restaurant.id}/tables/available/count`);
            if (response.ok) {
                const data = await response.json();
                if (data > 0) {
                    availableRestaurants.push(restaurant);
                }
            }
        } catch (error) {
            console.error(`Error checking availability for restaurant ${restaurant.id}:`, error);
        }
    }

    return availableRestaurants;
}

/**
 * Apply sorting to restaurants
 */
function applySorting(restaurants, sortBy) {
    const sorted = [...restaurants];

    switch (sortBy) {
        case 'highest_rated':
            sorted.sort((a, b) => (b.rating || 0) - (a.rating || 0));
            break;
        case 'lowest_rated':
            sorted.sort((a, b) => (a.rating || 0) - (b.rating || 0));
            break;
        case 'least_expensive':
            sorted.sort((a, b) => (a.priceRange || 0) - (b.priceRange || 0));
            break;
        case 'most_expensive':
            sorted.sort((a, b) => (b.priceRange || 0) - (a.priceRange || 0));
            break;
        default:
            break;
    }

    return sorted;
}

// ==================== DISPLAY RESTAURANTS ====================
function displayRestaurants(restaurants) {
    const container = document.getElementById('restaurantCards');
    container.innerHTML = ''; // Clear existing cards

    if (restaurants.length === 0) {
        container.innerHTML = '<p style="text-align: center; color: #999;">No restaurants found.</p>';
        return;
    }

    const template = document.getElementById('restaurantCardTemplate');

    restaurants.forEach(restaurant => {
        const clone = template.content.cloneNode(true);

        // Populate card data
        const card = clone.querySelector('.restaurant-card');
        card.setAttribute('data-id', restaurant.id);

        clone.querySelector('.restaurant-image').src = restaurant.image || '/images/placeholder.png';
        clone.querySelector('.restaurant-image').alt = restaurant.name;
        clone.querySelector('.restaurant-title').textContent = restaurant.name;
        clone.querySelector('.restaurant-description').textContent = restaurant.description || 'No description available';
        clone.querySelector('.restaurant-rating').textContent = (restaurant.rating || 0).toFixed(1);

        // Add click event to navigate to detail page
        card.addEventListener('click', () => {
            navigateToRestaurantDetail(restaurant.id);
        });

        container.appendChild(clone);
    });
}

/**
 * Navigate to restaurant detail page
 */
function navigateToRestaurantDetail(restaurantId) {
    window.location.href = `/restaurant-detail.html?id=${restaurantId}`;
    // TODO: Create restaurant-detail.html page
}

// ==================== CAROUSEL ====================
async function initializeCarousel() {
    try {
        // Get restaurants with available tables
        const availableRestaurants = await filterByAvailability(allRestaurants);

        if (availableRestaurants.length === 0) {
            console.log('No restaurants with available tables for carousel');
            return;
        }

        allRestaurants = availableRestaurants; // Update for carousel use
        currentCarouselIndex = 0;
        displayCarouselSlide(allRestaurants[currentCarouselIndex]);

        // Show carousel
        document.getElementById('carousel').style.display = 'block';

        // Setup autoplay
        startCarouselAutoplay(allRestaurants);
    } catch (error) {
        console.error('Error initializing carousel:', error);
    }
}

/**
 * Display a single carousel slide
 */
function displayCarouselSlide(restaurant) {
    if (!restaurant) return;

    document.getElementById('carouselImage').src = restaurant.image || '/images/placeholder.png';
    document.getElementById('carouselImage').alt = restaurant.name;
    document.getElementById('carouselTitle').textContent = restaurant.name;
    document.getElementById('carouselDescription').textContent = restaurant.description || 'No description available';
    document.getElementById('carouselRating').textContent = (restaurant.rating || 0).toFixed(1);
}

/**
 * Start carousel autoplay
 */
function startCarouselAutoplay(restaurants) {
    if (carouselInterval) {
        clearInterval(carouselInterval);
    }

    carouselInterval = setInterval(() => {
        currentCarouselIndex = (currentCarouselIndex + 1) % restaurants.length;
        displayCarouselSlide(restaurants[currentCarouselIndex]);
    }, CAROUSEL_INTERVAL);
}

/**
 * Stop carousel autoplay (useful if needed)
 */
function stopCarouselAutoplay() {
    if (carouselInterval) {
        clearInterval(carouselInterval);
    }
}

// ==================== LOGOUT ====================
document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            logout(); // Uses function from jwt.js
        });
    }
});

// ==================== NOTIFICATIONS ====================
function showAlert(message) {
    const alertElement = document.getElementById('alertEvent');
    const messageElement = document.getElementById('alertMessage');

    if (alertElement && messageElement) {
        messageElement.textContent = message;
        alertElement.style.display = 'block';

        // Auto-hide after 5 seconds
        setTimeout(() => {
            alertElement.style.display = 'none';
        }, 5000);
    }
}

// ==================== FOOTER ====================
document.addEventListener('DOMContentLoaded', () => {
    const currentYear = new Date().getFullYear();
    const copyrightElement = document.getElementById('copyright');
    if (copyrightElement) {
        copyrightElement.textContent = `© ${currentYear} RestaurantBooking. All rights reserved.`;
    }
});

