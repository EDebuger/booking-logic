// Restaurant Detail Page Script

let restaurantId;
let selectedTable = null;

document.addEventListener('DOMContentLoaded', async () => {
    // Check authentication
    if (!isLoggedin()) {
        window.location.href = 'http://localhost:8080/auth/login';
        return;
    }

    // Initialize page
    updateUserInfo();
    initializeEventListeners();
    restaurantId = getRestaurantIdFromURL();

    if (restaurantId) {
        await loadRestaurantDetails();
    } else {
        console.error('No restaurant ID provided');
        window.location.href = '/index.html';
    }

    // Set minimum date to today
    const dateInput = document.getElementById('dateInput');
    const today = new Date().toISOString().split('T')[0];
    dateInput.min = today;
});

/**
 * Get restaurant ID from URL query parameters
 */
function getRestaurantIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

/**
 * Load restaurant details from backend
 */
async function loadRestaurantDetails() {
    try {
        const response = await apiFetch(
            `http://localhost:8080/restaurants/getById/${restaurantId}`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch restaurant details');
        }

        const restaurant = await response.json();
        displayRestaurantInfo(restaurant);
    } catch (error) {
        console.error('Error loading restaurant details:', error);

        showNotification('Error loading restaurant details', 'error');
    }
}

/**
 * Display restaurant information on the page
 */
function displayRestaurantInfo(restaurant) {
    document.getElementById('restaurantName').textContent = restaurant.name;
    document.getElementById('restaurantImage').src = restaurant.image;
    document.getElementById('restaurantImage').alt = restaurant.name;
    document.getElementById('restaurantDescription').textContent = restaurant.description;
    document.getElementById('restaurantAddress').textContent = `📍 ${restaurant.adress}`;
    document.getElementById('restaurantPostalCode').textContent = `📮 ${restaurant.postalCode}`;
    document.getElementById('restaurantRating').textContent = `⭐ ${restaurant.rating}/5`;
    document.getElementById('restaurantServiceType').textContent = `${restaurant.serviceType}`;
}

/**
 * Initialize event listeners
 */
function initializeEventListeners() {
    // Back button
    document.getElementById('backBtn').addEventListener('click', () => {
        window.location.href = '/index.html';
    });

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', logout);

    // Date and party size selection
    document.getElementById('dateInput').addEventListener('change', loadAvailableTables);
    document.getElementById('partySizeInput').addEventListener('change', loadAvailableTables);

    // Modal actions
    document.getElementById('cancelBtn').addEventListener('click', closeBookingModal);
    document.getElementById('modalOverlay').addEventListener('click', closeBookingModal);
    document.getElementById('confirmBtn').addEventListener('click', confirmBooking);
}

/**
 * Load available tables based on date and party size
 */
async function loadAvailableTables() {
    const date = document.getElementById('dateInput').value;
    const partySize = document.getElementById('partySizeInput').value;

    if (!date || !partySize) {
        document.getElementById('tablesList').innerHTML =
            '<p class="placeholder">Select a date and party size to see available tables</p>';
        return;
    }

    try {
        const response = await apiFetch(
            `http://localhost:8080/restaurants/${restaurantId}/tables/available?partySize=${partySize}&date=${date}`
        );

        if (!response.ok) {
            throw new Error('Failed to fetch available tables');
        }

        const tables = await response.json();
        displayAvailableTables(tables);
    } catch (error) {
        console.error('Error loading available tables:', error);
        document.getElementById('tablesList').innerHTML =
            '<p class="placeholder">No tables available for the selected date and party size</p>';
    }
}

/**
 * Display available tables as clickable cards
 */
function displayAvailableTables(tables) {
    const tablesList = document.getElementById('tablesList');

    if (!tables || tables.length === 0) {
        tablesList.innerHTML =
            '<p class="placeholder">No tables available for the selected date and party size</p>';
        return;
    }

    tablesList.innerHTML = '';

    tables.forEach((table) => {
        const tableCard = document.createElement('div');
        tableCard.className = 'table-card';
        tableCard.innerHTML = `
            <div class="table-number">Table ${table.tableNumber}</div>
            <div class="table-section">${table.section}</div>
            <div class="table-capacity">Capacity: ${table.capacity}</div>
        `;

        tableCard.addEventListener('click', () => selectTable(table, tableCard));
        tablesList.appendChild(tableCard);
    });
}

/**
 * Select a table and highlight it
 */
function selectTable(table, cardElement) {
    // Remove selection from previously selected card
    document.querySelectorAll('.table-card.selected').forEach((card) => {
        card.classList.remove('selected');
    });

    // Select new table
    selectedTable = table;
    cardElement.classList.add('selected');

    // Show booking modal
    showBookingModal();
}

/**
 * Show booking confirmation modal
 */
function showBookingModal() {
    if (!selectedTable) return;

    const date = document.getElementById('dateInput').value;
    const partySize = document.getElementById('partySizeInput').value;

    const bookingDetails = document.getElementById('bookingDetails');
    bookingDetails.innerHTML = `
        <div class="booking-detail-row">
            <span class="booking-detail-label">Restaurant:</span>
            <span class="booking-detail-value">${document.getElementById('restaurantName').textContent}</span>
        </div>
        <div class="booking-detail-row">
            <span class="booking-detail-label">Date:</span>
            <span class="booking-detail-value">${formatDate(date)}</span>
        </div>
        <div class="booking-detail-row">
            <span class="booking-detail-label">Party Size:</span>
            <span class="booking-detail-value">${partySize} Guests</span>
        </div>
        <div class="booking-detail-row">
            <span class="booking-detail-label">Table Number:</span>
            <span class="booking-detail-value">${selectedTable.tableNumber}</span>
        </div>
        <div class="booking-detail-row">
            <span class="booking-detail-label">Section:</span>
            <span class="booking-detail-value">${selectedTable.section}</span>
        </div>
        <div class="booking-detail-row">
            <span class="booking-detail-label">Table Capacity:</span>
            <span class="booking-detail-value">${selectedTable.capacity} Seats</span>
        </div>
    `;

    document.getElementById('modalOverlay').classList.remove('hidden');
    document.getElementById('bookingModal').classList.remove('hidden');
}

/**
 * Close booking confirmation modal
 */
function closeBookingModal() {
    document.getElementById('modalOverlay').classList.add('hidden');
    document.getElementById('bookingModal').classList.add('hidden');
    selectedTable = null;
    document.querySelectorAll('.table-card.selected').forEach((card) => {
        card.classList.remove('selected');
    });
}

/**
 * Confirm booking and send to backend
 */
async function confirmBooking() {
    if (!selectedTable) return;

    const date = document.getElementById('dateInput').value;
    const partySize = document.getElementById('partySizeInput').value;

    const bookingData = {
        restaurantId: parseInt(restaurantId),
        tableId: selectedTable.id,
        bookingDate: date,
        partySize: parseInt(partySize),
        userId: getUserIdFromToken(),
    };

    try {
        const response = await apiFetch('http://localhost:8080/bookings/postBooking', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(bookingData),
        });

        if (!response.ok) {
            throw new Error('Failed to confirm booking');
        }

        // Booking successful
        closeBookingModal();
        showNotification('Booking confirmed! Redirecting to home page...', 'success');

        // Redirect to main page after 2 seconds
        setTimeout(() => {
            window.location.href = '/index.html';
        }, 2000);
    } catch (error) {
        console.error('Error confirming booking:', error);
        showNotification('Error confirming booking. Please try again.', 'error');
    }
}

/**
 * Extract user ID from JWT token
 */
function getUserIdFromToken() {
    const token = localStorage.getItem('authToken');
    if (!token) return null;

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.sub || payload.userId || payload.id;
    } catch (error) {
        console.error('Error decoding token:', error);
        return null;
    }
}

/**
 * Update user info in header (from jwt.js)
 */
function updateUserInfo() {
    const token = localStorage.getItem('authToken');
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.sub || 'User';
            document.getElementById('userInfo').textContent = `Welcome, ${username}`;
        } catch (error) {
            console.error('Error decoding token:', error);
        }
    }
}

/**
 * Format date for display (e.g., "2024-12-25" -> "Dec 25, 2024")
 */
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString + 'T00:00:00').toLocaleDateString('en-US', options);
}

/**
 * Show notification message
 */
function showNotification(message, type = 'info') {
    const banner = document.getElementById('notificationBanner');
    banner.textContent = message;
    banner.className = `notification-banner ${type}`;
    banner.classList.remove('hidden');

    setTimeout(() => {
        banner.classList.add('hidden');
    }, 4000);
}
