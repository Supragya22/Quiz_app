async function loadResults() {
    const response = await fetch("http://localhost:8080/api/tests/score/1/1", {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
    const score = await response.text();
    document.getElementById("score").innerText = `Your Score: ${score}`;
}

window.onload = loadResults;
