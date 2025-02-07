const API_BASE_URL = "http://localhost:8080/api";

// Retrieve token from localStorage
let token = localStorage.getItem("token");

// Login form submission
document.getElementById("loginForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("role").value;

    const loginData = { username, password};

    try {
        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginData)
        });

        if (!response.ok) {
            throw new Error("Invalid credentials");
        }

        const data = await response.json();
        console.log("Login Response:", data);  // Debugging: Check API response

        if (!data.token) {
            throw new Error("No token received from server");
        }

        // Store token and user details
        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);
        localStorage.setItem("username", data.username);

        // Redirect based on role
        if (data.role === "ROLE_ADMIN") {
            window.location.href = "admin-dashboard.html";
        } else {
            window.location.href = "participant-dashboard.html";
        }
    } catch (error) {
        console.error("Login Error:", error.message);
        document.getElementById("errorMessage").textContent = error.message;
    }
});

// Function to make an authenticated API request
async function fetchWithAuth(endpoint, options = {}) {
    const token = localStorage.getItem("token");
    if (!token) {
        console.error("No token found. Redirecting to login.");
        window.location.href = "index.html";
        return;
    }

    const headers = {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json",
        ...options.headers // Merge any additional headers
    };

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, { ...options, headers });
        if (!response.ok) {
            throw new Error(`API Error: ${response.statusText}`);
        }
        return await response.json();
    } catch (error) {
        console.error("API Fetch Error:", error.message);
        return null;
    }
}

// Example usage: Fetch all tests (Admin only)
async function fetchTests() {
    const data = await fetchWithAuth("/tests");
    console.log("Fetched Tests:", data);
}
