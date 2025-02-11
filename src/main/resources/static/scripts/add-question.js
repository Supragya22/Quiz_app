document.addEventListener("DOMContentLoaded", async function () {
    const testSelect = document.getElementById("testSelect");
    const questionList = document.getElementById("questionList");
    const assignButton = document.getElementById("assignQuestions");
    const messageElement = document.getElementById("message");

    async function fetchTests() {
        try {
            const response = await fetch("http://localhost:8080/api/tests/my-tests", {
                headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
            });
            const tests = await response.json();
            tests.forEach(test => {
                const option = document.createElement("option");
                option.value = test.id;
                option.textContent = test.name;
                testSelect.appendChild(option);
            });
        } catch (error) {
            console.error("Error fetching tests:", error);
        }
    }

    async function fetchQuestions() {
        try {
            const response = await fetch("http://localhost:8080/api/questions/my-questions", {
                headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
            });
            const questions = await response.json();
            questions.forEach(question => {
                const checkbox = document.createElement("input");
                checkbox.type = "checkbox";
                checkbox.value = question.id;
                checkbox.id = `question-${question.id}`;

                const label = document.createElement("label");
                label.htmlFor = `question-${question.id}`;
                label.textContent = question.questionText;

                const div = document.createElement("div");
                div.appendChild(checkbox);
                div.appendChild(label);
                questionList.appendChild(div);
            });
        } catch (error) {
            console.error("Error fetching questions:", error);
        }
    }

    assignButton.addEventListener("click", async function () {
        const selectedTestId = testSelect.value;
        const selectedQuestions = [...document.querySelectorAll("#questionList input:checked")].map(cb => parseInt(cb.value));

        if (!selectedTestId || selectedQuestions.length === 0) {
            messageElement.textContent = "Please select a test and at least one question.";
            messageElement.style.color = "red";
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/tests/${selectedTestId}/add-questions`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify({ questionIds: selectedQuestions })
            });

            if (response.ok) {
                messageElement.textContent = "Questions assigned successfully!";
                messageElement.style.color = "green";
                setTimeout(() => window.location.href = "admin-dashboard.html", 2000);
            } else {
                const errorText = await response.text();
                messageElement.textContent = `Failed to assign questions: ${errorText}`;
                messageElement.style.color = "red";
            }
        } catch (error) {
            console.error("Error assigning questions:", error);
            messageElement.textContent = "An error occurred while assigning questions.";
            messageElement.style.color = "red";
        }
    });

    await fetchTests();
    await fetchQuestions();
});
