const API_BASE_URL = "http://localhost:8080/api";
let token = localStorage.getItem("token");

document.getElementById("signupForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const newUsername = document.getElementById("newUsername").value;
    const newPassword = document.getElementById("newPassword").value;
    const newRole = document.getElementById("newRole").value;

    const signupData = { username: newUsername, password: newPassword, role: newRole };

    try {
        const response = await fetch("http://localhost:8080/api/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(signupData)
        });

        if (!response.ok) {
            throw new Error("Sign-up failed. Try again.");
        }

        document.getElementById("signupMessage").textContent = "Sign-up successful! You can now log in.";
        setTimeout(() => {
            window.location.href = "index.html";
        }, 2000); // Redirect after 2 seconds
    } catch (error) {
        document.getElementById("signupMessage").textContent = error.message;
    }
});
