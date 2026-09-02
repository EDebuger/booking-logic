// Global state
let currentUserId = null;
let allBookings = [];

// Initialize on page load
document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'http://localhost:8080/main.html';
        return;
    }

    try {
        const decoded = decodeToken(token);
        currentUserId = decoded.sub;

        await loadUserProfile();
        await loadUserBookings();
        initializeEventListeners();
    } catch (error) {
        console.error('Error initializing profile:', error);
        showNotification('Error loading profile', 'error');
    }
});

// Load user profile data
async function loadUserProfile() {
    try {
        const response = await apiFetch(`http://localhost:8080/users/getById/${currentUserId}`, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('Failed to fetch profile');
        }

        const profile = await response.json();

        // Populate profile fields
        document.getElementById('userName').textContent = profile.userName || '-';
        document.getElementById('userEmail').textContent = profile.email || '-';
        document.getElementById('userPhone').textContent = profile.phone || '-';
        document.getElementById('userRole').textContent = profile.userRole || '-';
        document.getElementById('memberSince').textContent = formatDate(profile.memberSince) || '-';
        document.getElementById('updatedAt').textContent = formatDate(profile.updatedAt) || '-';

        // Pre-fill edit form
        document.getElementById('editUserName').value = profile.userName || '';
        document.getElementById('editPhone').value = profile.phone || '';
    } catch (error) {
        console.error('Error loading profile:', error);
        showNotification('Error loading profile data', 'error');
    }
}

// Load user bookings
async function loadUserBookings() {
    try {
        const response = await apiFetch(`http://localhost:8080/bookings/getByUserId/${currentUserId}`, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error('Failed to fetch bookings');
        }

        allBookings = await response.json();
        sortAndDisplayBookings('date-newest');
    } catch (error) {
        console.error('Error loading bookings:', error);
        const bookingsList = document.getElementById('bookingsList');
        bookingsList.innerHTML = '<p class="no-bookings">No bookings found</p>';
    }
}

// Sort and display bookings
function sortAndDisplayBookings(sortBy) {
    let sorted = [...allBookings];

    switch (sortBy) {
        case 'date-newest':
            sorted.sort((a, b) => new Date(b.bookingDate) - new Date(a.bookingDate));
            break;
        case 'date-oldest':
            sorted.sort((a, b) => new Date(a.bookingDate) - new Date(b.bookingDate));
            break;
        case 'restaurant-a-z':
            sorted.sort((a, b) => (a.restaurantName || '').localeCompare(b.restaurantName || ''));
            break;
        case 'restaurant-z-a':
            sorted.sort((a, b) => (b.restaurantName || '').localeCompare(a.restaurantName || ''));
            break;
        case 'party-size':
            sorted.sort((a, b) => (a.partySize || 0) - (b.partySize || 0));
            break;
    }

    displayBookings(sorted);
}

// Display bookings as cards
function displayBookings(bookings) {
    const bookingsList = document.getElementById('bookingsList');

    if (bookings.length === 0) {
        bookingsList.innerHTML = '<p class="no-bookings">No bookings yet</p>';
        return;
    }

    bookingsList.innerHTML = bookings.map(booking => `
        <div class="booking-card">
            <div class="booking-info">
                <div class="booking-detail">
                    <span class="booking-detail-label">Restaurant</span>
                    <span class="booking-detail-value">${booking.restaurantName || '-'}</span>
                </div>
                <div class="booking-detail">
                    <span class="booking-detail-label">Date</span>
                    <span class="booking-detail-value">${formatDate(booking.bookingDate) || '-'}</span>
                </div>
                <div class="booking-detail">
                    <span class="booking-detail-label">Time</span>
                    <span class="booking-detail-value">${booking.bookingTime || '-'}</span>
                </div>
                <div class="booking-detail">
                    <span class="booking-detail-label">Party Size</span>
                    <span class="booking-detail-value">${booking.partySize || '-'} guests</span>
                </div>
                <div class="booking-detail">
                    <span class="booking-detail-label">Table</span>
                    <span class="booking-detail-value">${booking.tableNumber || '-'}</span>
                </div>
                <div class="booking-detail">
                    <span class="booking-detail-label">Status</span>
                    <span class="booking-detail-value">${booking.status || '-'}</span>
                </div>
            </div>
            <div class="booking-actions">
                <button class="delete-booking-btn" data-booking-id="${booking.id}">Delete</button>
            </div>
        </div>
    `).join('');

    // Add delete event listeners
    document.querySelectorAll('.delete-booking-btn').forEach(btn => {
        btn.addEventListener('click', (e) => deleteBooking(e.target.dataset.bookingId));
    });
}

// Delete booking
async function deleteBooking(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }

    try {
        const response = await apiFetch(`http://localhost:8080/bookings/deleteBooking/${bookingId}`, {
            method: 'POST',
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        showNotification('Booking cancelled successfully', 'success');
        await loadUserBookings();
    } catch (error) {
        console.error('Error deleting booking:', error);
        showNotification('Error cancelling booking', 'error');
    }
}

// Initialize event listeners
function initializeEventListeners() {
    // Edit profile button
    document.getElementById('editProfileBtn').addEventListener('click', openEditModal);

    // Modal close buttons
    document.getElementById('closeModalBtn').addEventListener('click', closeEditModal);
    document.getElementById('cancelEditBtn').addEventListener('click', closeEditModal);
    document.getElementById('modalOverlay').addEventListener('click', closeEditModal);

    // Booking sort select
    document.getElementById('bookingSortSelect').addEventListener('change', (e) => {
        sortAndDisplayBookings(e.target.value);
    });

    // Edit form submit
    document.getElementById('editForm').addEventListener('submit', handleEditSubmit);

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', logout);
}

// Open edit modal
function openEditModal() {
    document.getElementById('editModal').classList.remove('hidden');
}

// Close edit modal
function closeEditModal() {
    document.getElementById('editModal').classList.add('hidden');
    document.getElementById('editForm').reset();
}

// Handle edit form submission
async function handleEditSubmit(e) {
    e.preventDefault();

    const newUserName = document.getElementById('editUserName').value.trim();
    const newPhone = document.getElementById('editPhone').value.trim();
    const newPassword = document.getElementById('editPassword').value.trim();
    const currentPassword = document.getElementById('currentPassword').value.trim();

    if (!currentPassword) {
        showNotification('Current password is required', 'error');
        return;
    }

    try {
        // Validate fields that changed
        const validations = [];

        if (newUserName) {
            const validateUserNameRes = await apiFetch(
                `http://localhost:8080/users/${currentUserId}/validate-username`,
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userName: newUserName }),
                }
            );
            if (!validateUserNameRes.ok) {
                const error = await validateUserNameRes.text();
                throw new Error(`Username validation failed: ${error}`);
            }
            validations.push({ field: 'userName', value: newUserName });
        }

        if (newPhone) {
            const validatePhoneRes = await apiFetch(
                `http://localhost:8080/users/${currentUserId}/validate-phone`,
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ phone: newPhone }),
                }
            );
            if (!validatePhoneRes.ok) {
                const error = await validatePhoneRes.text();
                throw new Error(`Phone validation failed: ${error}`);
            }
            validations.push({ field: 'phone', value: newPhone });
        }

        if (newPassword) {
            const validatePasswordRes = await apiFetch(
                `http://localhost:8080/users/${currentUserId}/validate-password`,
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ password: newPassword }),
                }
            );
            if (!validatePasswordRes.ok) {
                const error = await validatePasswordRes.text();
                throw new Error(`Password validation failed: ${error}`);
            }
            validations.push({ field: 'password', value: newPassword });
        }

        // All validations passed, now apply changes
        const applyChangesDto = {
            currentPassword: currentPassword,
            changes: validations.reduce((acc, v) => {
                acc[v.field] = v.value;
                return acc;
            }, {}),
        };

        const applyRes = await apiFetch(`http://localhost:8080/users/${currentUserId}/profile-changes`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(applyChangesDto),
        });

        if (!applyRes.ok) {
            const error = await applyRes.text();
            throw new Error(error);
        }

        showNotification('Profile updated successfully', 'success');
        closeEditModal();
        await loadUserProfile();
    } catch (error) {
        console.error('Error updating profile:', error);
        showNotification(error.message || 'Error updating profile', 'error');
    }
}

// Logout
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        localStorage.removeItem('token');
        window.location.href = 'http://localhost:8080/main.html';
    }
}

// Utility: Format date
function formatDate(dateString) {
    if (!dateString) return null;
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
    });
}

// Utility: Show notification
function showNotification(message, type = 'success') {
    const banner = document.getElementById('notificationBanner');
    banner.textContent = message;
    banner.className = `notification-banner ${type}`;

    setTimeout(() => {
        banner.classList.add('hidden');
    }, 4000);
}

// Utility: API fetch with token
async function apiFetch(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers,
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    return fetch(url, {
        ...options,
        headers,
    });
}

// Utility: Decode JWT token
function decodeToken(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('Error decoding token:', error);
        throw error;
    }
}
