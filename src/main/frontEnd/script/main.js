const API_BASE_URL = 'http://localhost:8080';

// Decode JWT and extract username from 'sub' claim
function decodeJWT() {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
        const parts = token.split('.');
        const decoded = JSON.parse(atob(parts[1]));
        return decoded.sub; // 'sub' claim contains the username
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

// Load all restaurants (guests allowed)
async function loadRestaurants() {
    try {
        const restaurants = await apiFetch('/restaurants/getAll');

        if (!window.allRestaurants || window.allRestaurants.length === 0) {
            window.allRestaurants = restaurants;
        }

        displayRestaurants(restaurants);
    } catch (error) {
        console.error('Error loading restaurants:', error);
    }
}

// Display restaurants in grid
function displayRestaurants(restaurants) {
    const container = document.getElementById('restaurantCards');
    if (!container) {
        console.error('Element #restaurantCards not found');
        return;
    }

    container.innerHTML = restaurants.map(restaurant => `
        <div class="restaurant-card" onclick="navigateToRestaurant(${restaurant.id})">
            <h3>${restaurant.name}</h3>
            <p>${restaurant.description}</p>
            <p>Rating: ${restaurant.rating}</p>
            <p>Price Range: ${restaurant.priceRange}</p>
        </div>
    `).join('');
}

// Navigate to restaurant detail page
function navigateToRestaurant(id) {
    window.location.href = `./restaurant-detail.html?id=${id}`;
}

// Search bar functionality (guests allowed)
async function handleSearch() {
    const searchInput = document.getElementById('searchBar')?.value || '';

    if (!searchInput.trim()) {
        loadRestaurants();
        return;
    }

    try {
        const results = await apiFetch(`/restaurants/getByName/${encodeURIComponent(searchInput)}`);
        displayRestaurants(results);
    } catch (error) {
        console.error('Search error:', error);
        alert('No restaurants found');
    }
}

// Filter by service type (guests allowed)
async function filterByServiceType(serviceType) {
    try {
        const results = await apiFetch(`/restaurants/getByServiceType/${serviceType}`);
        displayRestaurants(results);
    } catch (error) {
        console.error('Filter error:', error);
    }
}

// Filter by price range (guests allowed)
async function filterByPrice(minPrice, maxPrice) {
    try {
        const aboveMin = await apiFetch(`/restaurants/getPriceRangeWithin/${maxPrice}`);
        displayRestaurants(aboveMin);
    } catch (error) {
        console.error('Price filter error:', error);
    }
}

// Filter by rating (guests allowed)
async function filterByRating(minRating) {
    try {
        const results = await apiFetch(`/restaurants/getByRating/${minRating}`);
        displayRestaurants(results);
    } catch (error) {
        console.error('Rating filter error:', error);
    }
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
    window.location.href = './login.html'; // Adjust path based on your structure
}

// Update UI based on authentication status
function updateAuthUI() {
    const authButton = document.getElementById('logoutBtn'); // Button element
    const userNameDisplay = document.getElementById('userName'); // Username display

    if (isAuthenticated()) {
        const username = decodeJWT();

        // Show username
        if (userNameDisplay) {
            userNameDisplay.textContent = username;
            userNameDisplay.style.display = 'block';
        }

        // Change button to "Logout"
        if (authButton) {
            authButton.textContent = 'Logout';
            authButton.onclick = handleAuthButton;
        }
    } else {
        // Hide username
        if (userNameDisplay) {
            userNameDisplay.textContent = '';
            userNameDisplay.style.display = 'none';
        }

        // Change button to "Login"
        if (authButton) {
            authButton.textContent = 'Login';
            authButton.onclick = handleAuthButton;
        }
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    loadRestaurants();
    updateAuthUI();
});
