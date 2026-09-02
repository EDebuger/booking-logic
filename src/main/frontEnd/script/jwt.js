// ===== CORE API FUNCTIONS =====

// Connect to API with automatic token attachment
async function apiFetch(url, options = {}) {
    const token = localStorage.getItem("token");

    const response = await fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token && { "Authorization": `Bearer ${token}` }),
            ...options.headers
        }
    });

    if (response.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/main.html";
        return;
    }
    return response;
}

// Decode JWT token to extract user info (e.g., user ID from 'sub' claim)
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

// ===== AUTHENTICATION FUNCTIONS =====

// Login - receives username/password, stores token
async function login(username, password) {
    const response = await fetch("/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({userName: username, password: password})
    });

    if (!response.ok) {
        throw new Error("Wrong username or password");
    }

    const token = await response.text();
    localStorage.setItem("token", token);
    window.location.href = "../index/main.html";
}

// Check if user is logged in
function isLoggedin() {
    return localStorage.getItem("token") !== null;
}

// Logout - clear token and redirect
function logout() {
    localStorage.removeItem("token");
    window.location.href = "../index/main.html";
}

// ===== API HELPER FUNCTIONS =====

// Register new user
async function register(newUser) {
    const response = await apiFetch("/auth/register", {
        method: "POST",
        body: JSON.stringify(newUser)
    });

    if (!response.ok) {
        throw new Error("Registration failed");
    }

    return await response.json();
}

// Get all users (authenticated)
async function getAllUsers() {
    const response = await apiFetch("http://localhost:8080/users/getAllUsers");

    if (!response.ok) {
        throw new Error("Failed to fetch users");
    }

    return await response.json();
}

// Create booking (authenticated)
async function postBooking(newBooking) {
    const response = await apiFetch("http://localhost:8080/bookings/postBooking", {
        method: "POST",
        body: JSON.stringify(newBooking)
    });

    if (!response.ok) {
        throw new Error("Failed to create booking");
    }

    return await response.json();
}
