document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const testId = localStorage.getItem("selectedTestId");
     // Get testId from URL

    if (testId) {
        fetchScores(testId);
    } else {
        console.error("Test ID not found in URL.");
    }
});

async function fetchScores(testId) {
    console.log("Fetching scores for test ID:", testId);

    try {
        const response = await fetch(`http://localhost:8080/api/tests/${testId}/scores`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`,
             "Content-Type": "application/json"}
        });

        console.log("Response status:", response.status);

        if (!response.ok) {
            console.error("Error fetching scores:", response.statusText);
            return;
        }

        const scores = await response.json();
        console.log("Fetched scores:", scores);

        displayScores(scores);
    } catch (error) {
        console.error("Error fetching test scores:", error);
    }
}

function displayScores(scores) {
    const tableBody = document.getElementById("scoreTableBody");
    tableBody.innerHTML = ""; // Clear previous entries

    if (scores.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='2'>No scores available.</td></tr>";
        return;
    }

    scores.forEach(score => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${score.testTakerName}</td>
            <td>${score.score}</td>
        `;
        tableBody.appendChild(row);
    });
}
