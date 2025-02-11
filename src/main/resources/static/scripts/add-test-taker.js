document.addEventListener("DOMContentLoaded", function () {
    fetchTests();
    fetchUsers();
});

async function fetchTests() {
    try {
        const response = await fetch("http://localhost:8080/api/tests/my-tests", {
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch tests");
        }

        const tests = await response.json();
        const testSelect = document.getElementById("testSelect"); // Define testSelect

        tests.forEach(test => {
            const option = document.createElement("option");
            option.value = test.id;
            option.textContent = test.name;
            testSelect.appendChild(option);
        });

    } catch (error) {
        console.error("Error fetching tests:", error);
    }
}

async function fetchUsers() {
    try {
        const response = await fetch("http://localhost:8080/api/users/test-takers", {
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch users");
        }

        const users = await response.json();
        const userSelect = document.getElementById("userSelect"); // Define userSelect

        users.forEach(user => {
            const option = document.createElement("option");
            option.value = user.id;
            option.textContent = user.username;
            userSelect.appendChild(option);
        });

    } catch (error) {
        console.error("Error fetching users:", error);
    }
}

function assignTestTaker() {
    let testId = document.getElementById("testSelect").value;
    let userId = document.getElementById("userSelect").value;
    let token = localStorage.getItem("token");

    if (!testId || !userId) {
        document.getElementById("message").textContent = "Please select both a test and a user.";
        document.getElementById("message").style.color = "red";
        return;
    }

    fetch(`http://localhost:8080/api/tests/${testId}/assign/${userId}`, { // Corrected string interpolation
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`, // Fixed Authorization header syntax
            "Content-Type": "application/json"
        }
    })
    .then(response => response.text())
    .then(message => {
        document.getElementById("message").textContent = message;
        document.getElementById("message").style.color = "green";
    })
    .catch(error => {
        console.error("Error assigning test-taker:", error);
        document.getElementById("message").textContent = "Error assigning test-taker.";
        document.getElementById("message").style.color = "red";
    });
}

// Attach event listener to button
document.getElementById("assignTestButton").addEventListener("click", assignTestTaker);
