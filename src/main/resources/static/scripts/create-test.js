document.getElementById("createTestForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const name = document.getElementById("testName").value.trim();
    const description = document.getElementById("testDescription").value.trim();
    const startTimeInput = document.getElementById("startTime").value;
    const endTimeInput = document.getElementById("endTime").value;
    const messageElement = document.getElementById("message");

    // Check if fields are empty
    if (!name || !description || !startTimeInput || !endTimeInput) {
        messageElement.innerText = "All fields are required!";
        messageElement.style.color = "red";
        return;
    }

    // Convert input to Date objects
    const startDate = new Date(startTimeInput);
    const endDate = new Date(endTimeInput);

    // Ensure valid start and end time
    if (endDate <= startDate) {
        messageElement.innerText = "End time must be after start time!";
        messageElement.style.color = "red";
        return;
    }

    // Function to format datetime as 'yyyy-MM-dd HH:mm:ss' (in local time)
    function formatDateTime(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        const seconds = "00"; // Setting seconds to "00"

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    // Format startTime and endTime correctly
    const startTime = formatDateTime(startDate);
    const endTime = formatDateTime(endDate);

    console.log("Formatted Start Time:", startTime);
    console.log("Formatted End Time:", endTime);

    const requestBody = {
        name,
        description,
        startTime,
        endTime,
        active: true
    };

    try {
        const response = await fetch("http://localhost:8080/api/tests", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            messageElement.innerText = "Test created successfully!";
            messageElement.style.color = "green";
            setTimeout(() => window.location.href = "admin-dashboard.html", 2000);
        } else {
            const errorText = await response.text();
            messageElement.innerText = `Failed to create test: ${errorText}`;
            messageElement.style.color = "red";
        }
    } catch (error) {
        console.error("Error creating test:", error);
        messageElement.innerText = "An error occurred while creating the test.";
        messageElement.style.color = "red";
    }
});