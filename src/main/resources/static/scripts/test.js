const API_BASE_URL = "http://localhost:8080";
let testId = localStorage.getItem("testId");
let userId = localStorage.getItem("id");
let token = localStorage.getItem("token");
let questions = [];
let currentQuestionIndex = 0;
let endTime = null;
let timerInterval = null;

document.addEventListener("DOMContentLoaded", () => {
    startTestOnLoad(); // Start test when test.html loads
});

// Start the test when test.html loads
async function startTestOnLoad() {
    if (!testId || !userId || !token) {
        console.error("Missing testId, userId, or token!");
        return;
    }

    fetch(`${API_BASE_URL}/api/test-takers/${userId}/start-test/${testId}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        })
        .then(response => response.json())
        .then(data => {
            questions = data;
            if (questions.length === 0) {
                alert("No questions available for this test.");
                return;
            }

            fetchTestEndTime();
            displayQuestion();
        })
        .catch(error => console.error("Error fetching test details:", error));
}

// Fetch test questions
//function fetchTestDetails() {
//    fetch(`${API_BASE_URL}/api/test-takers/${userId}/start-test/${testId}`, {
//        method: "GET",
//        headers: { "Authorization": `Bearer ${token}` }
//    })
//    .then(response => response.json())
//    .then(data => {
//        questions = data;
//        if (questions.length === 0) {
//            alert("No questions available for this test.");
//            return;
//        }
//
//        fetchTestEndTime();
//        displayQuestion();
//    })
//    .catch(error => console.error("Error fetching test details:", error));
//}

// Fetch test end time for countdown timer
function fetchTestEndTime() {
    fetch(`${API_BASE_URL}/api/tests/${testId}`, {
        method: "GET",
        headers: { "Authorization": `Bearer ${token}` }
    })
    .then(response => response.json())
    .then(data => {
        endTime = new Date(data.endTime).getTime();
        startTimer();
    })
    .catch(error => console.error("Error fetching test end time:", error));
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
        optionButton.classList.add("option-button");
        optionButton.onclick = () => saveAnswer(questionData.id, index + 1);

        let savedAnswer = localStorage.getItem(`answer_${questionData.id}`);
        if (savedAnswer && parseInt(savedAnswer) === index + 1) {
            optionButton.style.backgroundColor = "lightblue";
        }

        optionsDiv.appendChild(optionButton);
    });
}

// Save user's selected answer
function saveAnswer(questionId, selectedOption) {
    localStorage.setItem(`answer_${questionId}`, selectedOption);
    displayQuestion();
}

// Navigate questions
function prevQuestion() { if (currentQuestionIndex > 0) currentQuestionIndex--; displayQuestion(); }
function nextQuestion() { if (currentQuestionIndex < questions.length - 1) currentQuestionIndex++; displayQuestion(); }

// Submit test manually
function submitTest() {
    let answers = questions.map(q => {
        let savedAnswer = localStorage.getItem(`answer_${q.id}`);
        return savedAnswer ? { question: q.id, selectedOption: parseInt(savedAnswer) } : null;
    }).filter(a => a !== null);

    fetch(`${API_BASE_URL}/api/test-takers/${userId}/submit-test/${testId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
        body: JSON.stringify(answers)
    })
    .then(() => { clearInterval(timerInterval); fetchTestResult(); })
    .catch(error => console.error("Error submitting test:", error));
}

// Fetch and display test score
function fetchTestResult() {
    fetch(`${API_BASE_URL}/api/test-takers/${userId}/test-result/${testId}`, {
        method: "GET",
        headers: { "Authorization": `Bearer ${token}` }
    })
    .then(response => response.json())
    .then(data => showPopup(data.score))
    .catch(error => console.error("Error fetching test result:", error));
}

// Show popup with score
function showPopup(score) { document.getElementById("popup-message").innerHTML = `Your Score: <b>${score}</b>`; document.getElementById("popup-box").style.display = "block"; }
function closePopup() { document.getElementById("popup-box").style.display = "none"; window.location.href = "participant-dashboard.html"; }

// Logout
function logout() { localStorage.clear(); window.location.href = "index.html"; }
