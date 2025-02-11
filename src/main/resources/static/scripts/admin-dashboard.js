async function loadTests() {
    const response = await fetch("http://localhost:8080/api/tests/my-tests", {
    method: "GET",
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });

    const tests = await response.json();
    console.log("TEST INFO: ", tests);
    const tbody = document.getElementById("testsTable").querySelector("tbody");
    tbody.innerHTML = "";

    tests.forEach(test => {
        const row = tbody.insertRow();
        row.innerHTML = `<td>${test.name}</td><td>${test.description}</td><td>${test.active ? "Active" : "Inactive"}</td>`;
    });
}
async function createTest() {
    window.location.href = "create-test.html";
    loadTests();
}
async function createQuestion() {
    window.location.href = "create-question.html";
    loadTests();
}
async function addQuestions() {
    window.location.href = "add-question.html";
    loadTests();
}
async function addTestTakers() {
    window.location.href = "add-test-taker.html";
    loadTests();
}

function logout() {
    // Clear any authentication tokens or session storage
    localStorage.removeItem("authToken");
    localStorage.removeItem("username");
    sessionStorage.clear();

    // Redirect to login page
    window.location.href = "index.html";
  }
window.onload = loadTests;
