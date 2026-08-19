// connect to api

// login that recieves token, afterwards the rest are available
async function login(username, password) {
    const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"}, // info
        body: JSON.stringify({userName: "", password: ""}), // params
    });
    if (!response.ok) {
        throw new Error("Wrong username or password");
    }
    const token = await response.text();
    localStorage.setItem("token", token);
}

const response = await fetch("http://localhost:8080/auth/testGetUsers", {
    method: "GET",
    headers: {"Content-Type": "application/json"},
    "Authorization": `Bearer ${localStorage.getItem("token")}` // get item called 'token'
}   );

const response = await fetch("http://localhost:8080/users/getAllUsers", {
    method: "GET",
    headers: {"Content-Type": "application/json"},
    "Authorization": `Bearer ${localStorage.getItem("token")}` // get item called 'token'
}   );


async function isLoggedin() {
    return localStorage.getItem("token") !== null;
}

async function logout() {
    localStorage.removeItem("token");
    window.location.href = "http://localhost:8080/auth/login";
}