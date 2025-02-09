const userId = 2; // Get userId from localStorage
const API_BASE_URL = "http://localhost:8080/api"; // Change based on your backend

document.addEventListener("DOMContentLoaded", function () {
    fetchAssignedTests();
});

async function fetchAssignedTests() {
    const response = await fetch(`${API_BASE_URL}/test-takers/${userId}/tests`, {
        method: "GET",
        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
    });
    const tests = await response.json();

    let testList = document.getElementById("assigned-tests");
    testList.innerHTML = "";

    tests.forEach(test => {
        let li = document.createElement("li");
        li.innerHTML = `${test.name} <button onclick="startTest(${test.id})">Start Test</button>`;
        testList.appendChild(li);
    });
}

async function startTest(testId) {
    const response = await fetch(`${API_BASE_URL}/tests/start/${userId}/${testId}`);
    const testData = await response.json();

    document.getElementById("test-list").style.display = "none";
    document.getElementById("test-section").style.display = "block";
    document.getElementById("test-title").innerText = testData.name;

    let questionContainer = document.getElementById("question-container");
    questionContainer.innerHTML = "";

    testData.questions.forEach((question, index) => {
        let questionDiv = document.createElement("div");
        questionDiv.innerHTML = `
            <p><b>${index + 1}. ${question.questionText}</b></p>
            <input type="radio" name="q${question.id}" value="1"> ${question.option1} <br>
            <input type="radio" name="q${question.id}" value="2"> ${question.option2} <br>
            <input type="radio" name="q${question.id}" value="3"> ${question.option3} <br>
            <input type="radio" name="q${question.id}" value="4"> ${question.option4} <br>
        `;
        questionContainer.appendChild(questionDiv);
    });

    startTimer(testData.duration);
}

function startTimer(duration) {
    let timerElement = document.getElementById("timer");
    let timeRemaining = duration * 60;

    const timerInterval = setInterval(() => {
        let minutes = Math.floor(timeRemaining / 60);
        let seconds = timeRemaining % 60;
        timerElement.innerText = `Time Left: ${minutes}m ${seconds}s`;

        if (timeRemaining <= 0) {
            clearInterval(timerInterval);
            submitTest();
        }

        timeRemaining--;
    }, 1000);
}

async function submitTest() {
    let answers = [];
    document.querySelectorAll("#question-container div").forEach(div => {
        let questionId = div.querySelector("input").name.replace("q", "");
        let selectedOption = div.querySelector("input:checked");
        if (selectedOption) {
            answers.push({ questionId: parseInt(questionId), selectedOptionIndex: parseInt(selectedOption.value) });
        }
    });

    const response = await fetch(`${API_BASE_URL}/${userId}/submit-test/${testId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ answers })
    });

    if (response.ok) {
        fetchTestScore();
    }
}

async function fetchTestScore() {
    const response = await fetch(`${API_BASE_URL}/tests/score/${userId}/${testId}`);
    const data = await response.json();

    document.getElementById("test-section").style.display = "none";
    document.getElementById("score-section").style.display = "block";
    document.getElementById("score").innerText = `You scored: ${data.score}`;
}
