// connect to api

// login that recieves token, afterwards the rest are available
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

//login
async function login(username, password) {
    const response = await fetch("/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({userName: "", password: ""})
    });
    if (!response.ok) {throw new Error("Wrong username or password");}

    const token = response.text();
    localStorage.setItem("token",token);
}

// simplified, GETTERS
const response = await fetch("http://localhost:8080/auth/testGetUsers");

const response = await fetch("http://localhost:8080/users/getAllUsers");

// simplified, SETTERS
const response = await fetch("http://localhost:8080/auth/register", {
    method: "POST",
    body: JSON.stringify(newUser),
});

const response = await fetch("http://localhost:8080/bookings/postBooking", {
    method: "POST",
    body: JSON.stringify(newBooking)
});

//login/logout functions
async function isLoggedin() {
    return localStorage.getItem("token") !== null;
}

async function logout() {
    localStorage.removeItem("token");
    window.location.href = "/main.html";
}