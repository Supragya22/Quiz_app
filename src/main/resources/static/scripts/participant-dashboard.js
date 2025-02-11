document.addEventListener("DOMContentLoaded", () => {
    fetchAssignedTests();
    setInterval(fetchAssignedTests, 60000); // Auto-refresh every 60 seconds
});

// Fetch assigned tests
function fetchAssignedTests() {
    let userId = localStorage.getItem("userId");
    let token = localStorage.getItem("token");

    fetch(`/api/tests/assigned/${userId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(tests => {
        let testList = document.getElementById("assigned-tests");
        testList.innerHTML = "";

        if (tests.length === 0) {
            testList.innerHTML = "<p>No assigned tests available.</p>";
            return;
        }

        tests.forEach(test => {
            let listItem = document.createElement("li");
            let statusText = test.isActive ? "Active" : "Inactive";
            let statusClass = test.isActive ? "active" : "inactive";

            listItem.innerHTML = `
                ${test.testName} - <span class="${statusClass}">${statusText}</span>
                ${test.isActive ? `<button onclick="startTest(${test.id})">Start Test</button>` : ""}
            `;

            testList.appendChild(listItem);
        });
    })
    .catch(error => console.error("Error fetching assigned tests:", error));
}

// Function to start test
function startTest(testId) {
    let userId = localStorage.getItem("userId");
    let token = localStorage.getItem("token");

    fetch(`/api/test-takers/${userId}/start-test/${testId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            localStorage.setItem("currentTestId", testId);
            window.location.href = "test.html"; // Redirect to test page
        } else {
            alert("Failed to start the test. Please try again.");
        }
    })
    .catch(error => console.error("Error starting test:", error));
}
