async function loadTests() {
    const response = await fetch("http://localhost:8080/api/tests/my-tests", {
    method: "GET",
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });

    const tests = await response.json();
    const tbody = document.getElementById("testsTable").querySelector("tbody");
    tbody.innerHTML = "";

    tests.forEach(test => {
        const row = tbody.insertRow();
        row.innerHTML = `<td>${test.name}</td><td>${test.description}</td><td>${test.is_active ? "Active" : "Inactive"}</td>`;
    });
}

async function createTest() {
    const testName = prompt("Enter test name:");
    const testDescription = prompt("Enter test description:");

    await fetch("http://localhost:8080/api/tests", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify({ name: testName, description: testDescription, is_active: true })
    });

    loadTests();
}

window.onload = loadTests;
