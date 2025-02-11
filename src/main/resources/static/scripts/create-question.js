document.getElementById("createQuestionForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const questionText = document.getElementById("questionText").value.trim();
    const option1 = document.getElementById("option1").value.trim();
    const option2 = document.getElementById("option2").value.trim();
    const option3 = document.getElementById("option3").value.trim();
    const option4 = document.getElementById("option4").value.trim();
    const correctOptionIndex = parseInt(document.getElementById("correctOption").value);
    const messageElement = document.getElementById("message");

    if (!questionText || !option1 || !option2 || !option3 || !option4 || isNaN(correctOptionIndex)) {
        messageElement.innerText = "All fields are required!";
        messageElement.style.color = "red";
        return;
    }

    const requestBody = {
        questionText,
        option1,
        option2,
        option3,
        option4,
        correctOptionIndex
    };

    try {
        const response = await fetch("http://localhost:8080/api/questions/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            messageElement.innerText = "Question created successfully!";
            messageElement.style.color = "green";
            setTimeout(() => window.location.href = "admin-dashboard.html", 2000);
        } else {
            const errorText = await response.text();
            messageElement.innerText = `Failed to create question: ${errorText}`;
            messageElement.style.color = "red";
        }
    } catch (error) {
        console.error("Error creating question:", error);
        messageElement.innerText = "An error occurred while creating the question.";
        messageElement.style.color = "red";
    }
});
