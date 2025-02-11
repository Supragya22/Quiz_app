// app.js

let testId = localStorage.getItem("currentTestId");
let userId = localStorage.getItem("userId");
let token = localStorage.getItem("token");
let questions = [];
let currentQuestionIndex = 0;
let endTime = null; // Stores test end time

document.addEventListener("DOMContentLoaded", () => {
    fetchTestDetails();
});

// Fetch test details including start and end time
function fetchTestDetails() {
    fetch(`/api/tests/${testId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(data => {
        questions = data.questions;
        endTime = new Date(data.endTime).getTime(); // Convert to timestamp
        displayQuestion();
        startTimer();
    })
    .catch(error => console.error("Error fetching test details:", error));
}

// Display the current question
function displayQuestion() {
    if (questions.length === 0) return;

    let questionData = questions[currentQuestionIndex];
    document.getElementById("question").innerText = questionData.questionText;

    let optionsDiv = document.getElementById("options");
    optionsDiv.innerHTML = "";

    [questionData.option1, questionData.option2, questionData.option3, questionData.option4].forEach((option, index) => {
        let optionButton = document.createElement("button");
        optionButton.innerText = option;
        optionButton.onclick = () => saveAnswer(questionData.id, index + 1);
        optionsDiv.appendChild(optionButton);
    });
}

// Save user's selected answer
function saveAnswer(questionId, selectedOption) {
    localStorage.setItem(`answer_${questionId}`, selectedOption);
}

// Navigate to previous question
function prevQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        displayQuestion();
    }
}

// Navigate to next question
function nextQuestion() {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        displayQuestion();
    }
}

// Submit test manually
function submitTest() {
    let answers = [];

    questions.forEach(q => {
        let savedAnswer = localStorage.getItem(`answer_${q.id}`);
        if (savedAnswer) {
            answers.push({ questionId: q.id, selectedOption: parseInt(savedAnswer) });
        }
    });

    fetch(`/api/tests/${testId}/submit`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ userId, answers })
    })
    .then(response => response.json())
    .then(() => {
        fetchTestResult(); // Fetch and display score after submission
    })
    .catch(error => console.error("Error submitting test:", error));
}

// Fetch test score after submission
function fetchTestResult() {
    fetch(`/api/tests/${userId}/test-result/${testId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(data => {
        showPopup(data.score); // Show popup with score
    })
    .catch(error => console.error("Error fetching test result:", error));
}

// Show popup box with score
function showPopup(score) {
    let popup = document.getElementById("popup-box");
    let popupMessage = document.getElementById("popup-message");
    popupMessage.innerHTML = `Your Score: <b>${score}</b>`;
    popup.style.display = "block";
}

// Close the popup
function closePopup() {
    document.getElementById("popup-box").style.display = "none";
    window.location.href = "participant-dashboard.html"; // Redirect back to dashboard
}

// Start the countdown timer based on start and end time
function startTimer() {
    let timerDisplay = document.getElementById("timer");

    let timerInterval = setInterval(() => {
        let now = new Date().getTime();
        let timeLeft = endTime - now;

        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            alert("Time is up! Submitting test...");
            submitTest();
            return;
        }

        let minutes = Math.floor(timeLeft / 60000);
        let seconds = Math.floor((timeLeft % 60000) / 1000);

        timerDisplay.innerText = `Time Left: ${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
    }, 1000);
}

// Logout function
function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}
