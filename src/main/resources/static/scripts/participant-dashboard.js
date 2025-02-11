const API_BASE_URL = "http://localhost:8080";
const userId = localStorage.getItem("id");
const token = localStorage.getItem("token");


// Redirect to login if user is not authenticated
if (!token) {
    window.location.href = "index.html";
}

// Set the username
document.getElementById("username").textContent = localStorage.getItem("username");

// Fetch and display assigned tests
async function fetchAssignedTests() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/test-takers/${userId}/tests`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch assigned tests");
        }

        const tests = await response.json();
        const testsBody = document.getElementById("testsBody");
        testsBody.innerHTML = "";

        tests.forEach(test => {
            const row = document.createElement("tr");

            // Check if test is active
            const isActive = new Date(test.startTime) <= new Date() && new Date(test.endTime) > new Date();

            row.innerHTML = `
                <td>${test.name}</td>
                <td>${new Date(test.startTime).toLocaleString()}</td>
                <td>${new Date(test.endTime).toLocaleString()}</td>
                <td>${isActive ? "Active" : "Inactive"}</td>
                <td>
                    <button onclick="startTest(${test.id})" ${!isActive ? "disabled" : ""}>Start Test</button>
                </td>
                <td>
                    <button onclick="viewScore(${test.id})">View Score</button>
                </td>
            `;

            testsBody.appendChild(row);
        });

    } catch (error) {
        console.error("Error fetching tests:", error);
    }
}

// Start test
function startTest(testId) {
    console.log("Redirecting to test.html with Test ID:", testId);
    localStorage.setItem("testId", testId);
    window.location.href = "test.html";
}

// View score
async function viewScore(testId) {
    try {
        const response = await fetch(`${API_BASE_URL}/api/test-takers/${userId}/test-result/${testId}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error("Failed to fetch test result");
        }

        const data = await response.json();
        alert(`Your score: ${data.score}`);

    } catch (error) {
        console.error("Error fetching score:", error);
    }
}

function logout() {
    // Clear any authentication tokens or session storage
    localStorage.removeItem("authToken");
    localStorage.removeItem("username");
    sessionStorage.clear();

    // Redirect to login page
    window.location.href = "index.html";
  }

// Fetch tests on page load
window.onload = fetchAssignedTests;
