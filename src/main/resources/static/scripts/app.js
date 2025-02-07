const API_BASE_URL = "http://localhost:8080/api";
let token = localStorage.getItem("token");

let testId = localStorage.getItem("testId"); // Assume testId is stored after selecting a test
let questions = [];
let currentQuestionIndex = 0;
let answers = {}; // Store user answers

// Load Questions for the Test
async function loadTestQuestions() {
    const response = await fetch(`${API_BASE_URL}/tests/${testId}/questions`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    if (response.ok) {
        questions = await response.json();
        displayQuestion(0);
    } else {
        alert("Failed to load questions");
    }
}

// Display Question
function displayQuestion(index) {
    const container = document.getElementById("questionContainer");
    container.innerHTML = "";

    let question = questions[index];

    if (!question) return;

    let questionHTML = `
        <h3>${index + 1}. ${question.text}</h3>
        <div>
            ${question.options.map((option, i) => `
                <label>
                    <input type="radio" name="answer" value="${option}"
                        ${answers[question.id] === option ? "checked" : ""}
                        onchange="saveAnswer(${question.id}, '${option}')">
                    ${option}
                </label><br>
            `).join('')}
        </div>
    `;

    container.innerHTML = questionHTML;

    // Handle navigation button visibility
    document.getElementById("prevBtn").disabled = index === 0;
    document.getElementById("nextBtn").disabled = index === questions.length - 1;
}

// Save Answer
function saveAnswer(questionId, answer) {
    answers[questionId] = answer;
}

// Next Question
function nextQuestion() {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        displayQuestion(currentQuestionIndex);
    }
}

// Previous Question
function prevQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        displayQuestion(currentQuestionIndex);
    }
}

// Submit Test
async function submitTest() {
    const response = await fetch(`${API_BASE_URL}/tests/${testId}/submit`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(answers)
    });

    if (response.ok) {
        alert("Test submitted successfully!");
        window.location.href = "result.html";
    } else {
        alert("Submission failed");
    }
}

// Load the test when the page loads
document.addEventListener("DOMContentLoaded", loadTestQuestions);
