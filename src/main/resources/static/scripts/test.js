//const API_BASE_URL = "http://localhost:8080";
//let testId = localStorage.getItem("testId");
//let userId = localStorage.getItem("id");
//let token = localStorage.getItem("token");
//let questions = [];
//let currentQuestionIndex = 0;
//let endTime = null;
//let timerInterval = null;
//
//document.addEventListener("DOMContentLoaded", () => {
//    startTestOnLoad(); // Start test when test.html loads
//});
//
//// Start the test when test.html loads
//async function startTestOnLoad() {
//    if (!testId || !userId || !token) {
//        console.error("Missing testId, userId, or token!");
//        return;
//    }
//
//    fetch(`${API_BASE_URL}/api/test-takers/${userId}/start-test/${testId}`, {
//            method: "GET",
//            headers: { "Authorization": `Bearer ${token}` }
//        })
//        .then(response => response.json())
//        .then(data => {
//            questions = data;
//            if (questions.length === 0) {
//                alert("No questions available for this test.");
//                return;
//            }
//
//            fetchTestEndTime();
//            displayQuestion();
//        })
//        .catch(error => console.error("Error fetching test details:", error));
//}
//
//// Fetch test questions
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
//        let endTime = fetchTestEndTime();
//        console.log("END TIME: ", endTime);
//        displayQuestion();
//        document.getElementById("timer").innerText = `Timer: ${endTime}`;
//    })
//    .catch(error => console.error("Error fetching test details:", error));
//}
//
////Fetch test end time for countdown timer
//function fetchTestEndTime() {
//    fetch(`${API_BASE_URL}/api/tests/${testId}`, {
//        method: "GET",
//        headers: { "Authorization": `Bearer ${token}` }
//    })
//    .then(response => response.json())
//    .then(data => {
//        endTime = new Date(data.endTime).getTime();
//        console.log("END TIME IN FUNCTION: ", endTime);
//
//    })
//    .catch(error => console.error("Error fetching test end time:", error));
//}
//
//// Display the current question
//function displayQuestion() {
//    if (questions.length === 0) return;
//
//    let questionData = questions[currentQuestionIndex];
//    document.getElementById("question").innerText = questionData.questionText;
//
//    let optionsDiv = document.getElementById("options");
//    optionsDiv.innerHTML = "";
//
//    [questionData.option1, questionData.option2, questionData.option3, questionData.option4].forEach((option, index) => {
//        let optionButton = document.createElement("button");
//        optionButton.innerText = option;
//        optionButton.classList.add("option-button");
//        optionButton.onclick = () => saveAnswer(questionData.id, index + 1);
//
//        let savedAnswer = localStorage.getItem(`answer_${questionData.id}`);
//        if (savedAnswer && parseInt(savedAnswer) === index + 1) {
//            optionButton.style.backgroundColor = "lightblue";
//        }
//
//        optionsDiv.appendChild(optionButton);
//    });
//}
//
//// Save user's selected answer
//function saveAnswer(questionId, selectedOption) {
//    localStorage.setItem(`answer_${questionId}`, selectedOption);
//    displayQuestion();
//}
//
//// Navigate questions
//function prevQuestion() { if (currentQuestionIndex > 0) currentQuestionIndex--; displayQuestion(); }
//function nextQuestion() { if (currentQuestionIndex < questions.length - 1) currentQuestionIndex++; displayQuestion(); }
//
//// Submit test manually
//function submitTest() {
//    let answers = questions.map(q => {
//        let savedAnswer = localStorage.getItem(`answer_${q.id}`);
//        return savedAnswer ? { question: {id: q.id}, selectedOption: parseInt(savedAnswer) } : null;
//    }).filter(a => a !== null);
//
//    fetch(`${API_BASE_URL}/api/test-takers/${userId}/submit-test/${testId}`, {
//        method: "POST",
//        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
//        body: JSON.stringify(answers)
//    })
//    .then(() => { clearInterval(timerInterval); fetchTestResult(); })
//    .catch(error => console.error("Error submitting test:", error));
//}
//
//// Fetch and display test score
//function fetchTestResult() {
//    fetch(`${API_BASE_URL}/api/test-takers/${userId}/test-result/${testId}`, {
//        method: "GET",
//        headers: { "Authorization": `Bearer ${token}` }
//    })
//    .then(response => response.json())
//    .then(data => showPopup(data.score))
//    .catch(error => console.error("Error fetching test result:", error));
//}
//
//// Show popup with score
//function showPopup(score) { document.getElementById("popup-message").innerHTML = `Your Score: <b>${score}</b>`; document.getElementById("popup-box").style.display = "block"; }
//function closePopup() { document.getElementById("popup-box").style.display = "none"; window.location.href = "participant-dashboard.html"; }
//
//// Logout
//function logout() { localStorage.clear(); window.location.href = "index.html"; }
const API_BASE_URL = "http://localhost:8080";
let testId = localStorage.getItem("testId");
let userId = localStorage.getItem("id");
let token = localStorage.getItem("token");
let questions = [];
let currentQuestionIndex = 0;
let endTime = null;
let timerInterval = null;

document.addEventListener("DOMContentLoaded", () => {
    startTestOnLoad();
});

// Start the test when test.html loads
async function startTestOnLoad() {
    if (!testId || !userId || !token) {
        console.error("Missing testId, userId, or token!");
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/api/test-takers/${userId}/start-test/${testId}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        const data = await response.json();
        console.log("TEST DATA:", data);

        if (!response.ok) {
                    console.error("Error:", data.error);
                    alert(data.error); // Show error message if test is already submitted
                    window.location.href = "participant-dashboard.html"; // Redirect user to dashboard
                    return;
                }

        questions = data;
        if (questions.length === 0) {
            alert("No questions available for this test.");
            return;
        }

        endTime = await fetchTestEndTime();
        if (endTime) {
            startTimer();
        }
        displayQuestion();
    } catch (error) {
        console.error("Error fetching test details:", error);
    }
}

// Fetch test end time for countdown timer
async function fetchTestEndTime() {
console.log("LOG");
    try {

        const response = await fetch(`${API_BASE_URL}/api/tests/${testId}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        const data = await response.json();
        console.log("Test Data ", data);
        console.log("Data EndTime", data.endTime);
        endTime = new Date(data.endTime).getTime();
        console.log("End Time: ", endTime);
        return endTime;
    } catch (error) {
        console.error("Error fetching test end time:", error);
        return null;
    }
}

// Start countdown timer
function startTimer() {
    if (!endTime) {
        console.error("Timer cannot start: endTime is null");
        return;
    }

    timerInterval = setInterval(() => {
        let currentTime = new Date().getTime();
        let timeRemaining = endTime - currentTime;

        if (timeRemaining <= 0) {
            clearInterval(timerInterval);
            document.getElementById("timer").innerText = "Time's up!";
            submitTest();
        } else {
            let minutes = Math.floor((timeRemaining / 1000) / 60);
            let seconds = Math.floor((timeRemaining / 1000) % 60);
            document.getElementById("timer").innerText = `Timer: ${minutes}m ${seconds}s`;
        }
    }, 1000);
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

// Navigate between questions
function prevQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        displayQuestion();
    }
}

function nextQuestion() {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        displayQuestion();
    }
}

// Submit test manually
async function submitTest() {
    let answers = questions.map(q => {
        let savedAnswer = localStorage.getItem(`answer_${q.id}`);
        return savedAnswer ? { question: { id: q.id }, selectedOption: parseInt(savedAnswer) } : null;
    }).filter(a => a !== null);

    try {
        await fetch(`${API_BASE_URL}/api/test-takers/${userId}/submit-test/${testId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
            body: JSON.stringify(answers)
        });

        clearInterval(timerInterval);
        fetchTestResult();
    } catch (error) {
        console.error("Error submitting test:", error);
    }
}

// Fetch and display test score
async function fetchTestResult() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/test-takers/${userId}/test-result/${testId}`, {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        const data = await response.json();
        showPopup(data.score);
    } catch (error) {
        console.error("Error fetching test result:", error);
    }
}

// Show popup with score
function showPopup(score) {
    document.getElementById("popup-message").innerHTML = `Your Score: <b>${score}</b>`;
    document.getElementById("popup-box").style.display = "block";
}

function closePopup() {
    document.getElementById("popup-box").style.display = "none";
    window.location.href = "participant-dashboard.html";
}

// Logout
function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}
