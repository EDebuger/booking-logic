const API_BASE_URL = 'http://localhost:8080';

// Cache all restaurants for client-side filtering
window.allRestaurants = [];
window.filteredRestaurants = [];

// Decode JWT and extract username from 'sub' claim
function decodeJWT() {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
        const parts = token.split('.');
        const decoded = JSON.parse(atob(parts[1]));
        return decoded.sub;
    } catch (error) {
        console.error('Error decoding JWT:', error);
        return null;
    }
}

// Check if user is authenticated
function isAuthenticated() {
    return localStorage.getItem('token') !== null;
}

// Fetch with optional token
async function apiFetch(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : `${API_BASE_URL}${endpoint}`;
    const token = localStorage.getItem('token');

    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, {
        ...options,
        headers
    });

    if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
    }

    return response.json();
}

// Load all restaurants and cache them
async function loadRestaurants() {
    try {
        const restaurants = await apiFetch('/restaurants/getAll');
        window.allRestaurants = restaurants;
        window.filteredRestaurants = [...restaurants];
        displayRestaurants(restaurants);
        displayCarousel();
    } catch (error) {
        console.error('Error loading restaurants:', error);
        displayError('Failed to load restaurants');
    }
}


function displayCarousel() {
    const carousel = document.getElementById('carousel');
    if (!carousel || !window.allRestaurants || window.allRestaurants.length === 0) return;

    // Get top 3 highest rated restaurants
    const topRated = [...window.allRestaurants]
        .sort((a, b) => (b.rating || 0) - (a.rating || 0))
        .slice(0, 3);

    if (topRated.length === 0) return;

    let currentIndex = 0;

    function showRestaurant(index) {
        const restaurant = topRated[index];
        document.getElementById('carouselImage').src = restaurant.image;
        document.getElementById('carouselImage').alt = restaurant.name;
        document.getElementById('carouselTitle').textContent = restaurant.name;
        document.getElementById('carouselDescription').textContent = restaurant.description || '';
        document.getElementById('carouselRating').textContent = restaurant.rating || 'N/A';

        // Make carousel visible
        carousel.style.display = 'block';
    }

    // Show first restaurant
    showRestaurant(0);

    // Auto-rotate every 5 seconds
    setInterval(() => {
        currentIndex = (currentIndex + 1) % topRated.length;
        showRestaurant(currentIndex);
    }, 5000);

    // Click to navigate
    carousel.onclick = () => navigateToRestaurant(topRated[currentIndex].id);
}


// Display restaurants in grid
function displayRestaurants(restaurants) {
    const container = document.getElementById('restaurantCards');
    if (!container) {
        console.error('Element #restaurantCards not found');
        return;
    }

    if (restaurants.length === 0) {
        container.innerHTML = '<div class="empty-state"><p>No restaurants found</p></div>';
        return;
    }

    container.innerHTML = restaurants.map(restaurant => `
        <div class="restaurant-card" onclick="navigateToRestaurant(${restaurant.id})">
            <img src="${restaurant.image}" alt="${restaurant.name}" class="restaurant-image">
            <div class="restaurant-info">
                <h3 class="restaurant-title">${restaurant.name}</h3>
                <p class="restaurant-description">${restaurant.description || 'No description'}</p>
                <p><strong>Type:</strong> ${restaurant.serviceType || 'N/A'}</p>
                <p><strong>Price Range:</strong> ${restaurant.priceRange || 'N/A'}</p>
            </div>
            <div class="restaurant-footer">
                <span class="restaurant-rating">★ ${restaurant.rating || 'N/A'}</span>
            </div>
        </div>
    `).join('');
}


// Display error message
function displayError(message) {
    const container = document.getElementById('restaurantCards');
    if (container) {
        container.innerHTML = `<div class="error">${message}</div>`;
    }
}

// Navigate to restaurant detail page
function navigateToRestaurant(id) {
    window.location.href = `./details.html?id=${id}`;
}

// Apply all active filters
function applyFilters() {
    let filtered = [...window.allRestaurants];

    // Search filter
    const searchInput = document.getElementById('searchBar');
    if (searchInput && searchInput.value.trim()) {
        const query = searchInput.value.toLowerCase();
        filtered = filtered.filter(r =>
            r.name.toLowerCase().includes(query) ||
            r.description?.toLowerCase().includes(query)
        );
    }

    // Service type filter
    const serviceTypeSelect = document.getElementById('serviceTypeFilter');
    if (serviceTypeSelect && serviceTypeSelect.value) {
        filtered = filtered.filter(r => r.serviceType === serviceTypeSelect.value);
    }

    // Company/Brand filter
    const companySelect = document.getElementById('companyFilter');
    if (companySelect && companySelect.value) {
        filtered = filtered.filter(r => r.company === companySelect.value);
    }

    // Price range filter
    const sortSelect = document.getElementById('sortBySelect');
    if (sortSelect && sortSelect.value) {
        filtered = applySort(filtered, sortSelect.value);
    }

    // Available/Open now filter
    const availableCheckbox = document.getElementById('availableCheckbox');
    if (availableCheckbox && availableCheckbox.checked) {
        filtered = filtered.filter(r => r.isOpen === true);
    }

    window.filteredRestaurants = filtered;
    displayRestaurants(filtered);
}

// Sort restaurants
function applySort(restaurants, sortType) {
    const sorted = [...restaurants];

    switch (sortType) {
        case 'rating-high':
            sorted.sort((a, b) => (b.rating || 0) - (a.rating || 0));
            break;
        case 'rating-low':
            sorted.sort((a, b) => (a.rating || 0) - (b.rating || 0));
            break;
        case 'price-high':
            sorted.sort((a, b) => (b.priceRange || 0) - (a.priceRange || 0));
            break;
        case 'price-low':
            sorted.sort((a, b) => (a.priceRange || 0) - (b.priceRange || 0));
            break;
        case 'name-a-z':
            sorted.sort((a, b) => a.name.localeCompare(b.name));
            break;
        case 'name-z-a':
            sorted.sort((a, b) => b.name.localeCompare(a.name));
            break;
        default:
            return sorted;
    }

    return sorted;
}

// Get user profile (authenticated users only)
async function getUserProfile() {
    if (!isAuthenticated()) {
        console.warn('Not authenticated');
        return null;
    }

    try {
        const username = decodeJWT();
        if (!username) {
            console.error('No username found in token');
            return null;
        }

        const profile = await apiFetch('/auth/testGetUsers');
        return profile;
    } catch (error) {
        console.error('Error getting user profile:', error);
        return null;
    }
}

// Toggle login/logout
function handleAuthButton() {
    if (isAuthenticated()) {
        logout();
    } else {
        navigateToLogin();
    }
}

// Logout
function logout() {
    localStorage.removeItem('token');
    updateAuthUI();
    window.location.href = './index.html';
}

// Navigate to login page
function navigateToLogin() {
    window.location.href = './login.html';
}

// Update UI based on authentication status
function updateAuthUI() {
    const authButton = document.getElementById('logoutBtn');
    const userNameDisplay = document.getElementById('userName');

    if (isAuthenticated()) {
        const username = decodeJWT();

        if (userNameDisplay) {
            userNameDisplay.textContent = username;
            userNameDisplay.style.display = 'block';
        }

        if (authButton) {
            authButton.textContent = 'Logout';
            authButton.onclick = handleAuthButton;
        }
    } else {
        if (userNameDisplay) {
            userNameDisplay.textContent = '';
            userNameDisplay.style.display = 'none';
        }

        if (authButton) {
            authButton.textContent = 'Login';
            authButton.onclick = handleAuthButton;
        }
    }
}

// Initialize event listeners
function initializeEventListeners() {
    // Search bar
    const searchBar = document.getElementById('searchBar');
    if (searchBar) {
        searchBar.addEventListener('input', applyFilters);
    }

    // Service type filter
    const serviceTypeFilter = document.getElementById('serviceTypeFilter');
    if (serviceTypeFilter) {
        serviceTypeFilter.addEventListener('change', applyFilters);
    }

    // Company filter
    const companyFilter = document.getElementById('companyFilter');
    if (companyFilter) {
        companyFilter.addEventListener('change', applyFilters);
    }

    // Sort by dropdown
    const sortBySelect = document.getElementById('sortBySelect');
    if (sortBySelect) {
        sortBySelect.addEventListener('change', applyFilters);
    }

    // Available checkbox
    const availableCheckbox = document.getElementById('availableCheckbox');
    if (availableCheckbox) {
        availableCheckbox.addEventListener('change', applyFilters);
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    loadRestaurants();
    updateAuthUI();
    initializeEventListeners();
});
