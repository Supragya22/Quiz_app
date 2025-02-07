document.getElementById("createTestForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const name = document.getElementById("testName").value;
    const description = document.getElementById("testDescription").value;
    const startTime = new Date(document.getElementById("startTime").value).getTime();
    const endTime = new Date(document.getElementById("endTime").value).getTime();

    const response = await fetch("http://localhost:8080/api/tests", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify({ name, description, start_time: startTime, end_time: endTime, is_active: true })
    });

    if (response.ok) {
        document.getElementById("message").innerText = "Test created successfully!";
        setTimeout(() => window.location.href = "admin-dashboard.html", 2000);
    } else {
        document.getElementById("message").innerText = "Failed to create test!";
    }
});
