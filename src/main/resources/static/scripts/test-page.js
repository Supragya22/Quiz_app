let questions = [];
let currentQuestionIndex = 0;
let answers = {};

async function loadQuestions() {
    const response = await fetch("http://localhost:8080/api/tests/1/questions", {
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
    questions = await response.json();
    displayQuestion();
}

function displayQuestion() {
    const question = questions[currentQuestionIndex];
    document.getElementById("question-container").innerHTML = `
        <p>${question.text}</p>
        ${question.options.map((option, index) => `
            <label><input type="radio" name="answer" value="${index}"> ${option}</label>
        `).join("")}
    `;
}

function nextQuestion() {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        displayQuestion();
    }
}

function prevQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        displayQuestion();
    }
}

async function submitTest() {
    await fetch("http://localhost:8080/api/tests/submit", {
        method: "POST",
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` },
        body: JSON.stringify({ answers })
    });
    window.location.href = "results.html";
}

window.onload = loadQuestions;
