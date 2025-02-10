Directory structure:
quiz-app/  
│── src/  
│   ├── main/  
│   │   ├── java/com/practo/quiz/quiz_app/  
│   │   │   ├── QuizAppApplication.java  
│   │   │   │  
│   │   │   ├── controller/  
│   │   │   │   ├── AuthController.java  
│   │   │   │   ├── TestController.java  
│   │   │   │   ├── QuestionController.java  
│   │   │   │   ├── AnswerController.java  
│   │   │   │   ├── TestTakerController.java  
│   │   │   │
|   |   |   ├── dto/
│   │   │   │   ├── userDTO.java  
│   │   │   ├── model/  
│   │   │   │   ├── User.java  
│   │   │   │   ├── TestTaker.java  
│   │   │   │   ├── Test.java  
│   │   │   │   ├── Question.java  
│   │   │   │   ├── Answer.java  
│   │   │   │  
│   │   │   ├── repository/  
│   │   │   │   ├── UserRepository.java  
│   │   │   │   ├── TestTakerRepository.java  
│   │   │   │   ├── TestRepository.java  
│   │   │   │   ├── QuestionRepository.java  
│   │   │   │   ├── AnswerRepository.java  
│   │   │   │  
│   │   │   ├── service/  
│   │   │   │   ├── AuthService.java  
│   │   │   │   ├── TestService.java  
│   │   │   │   ├── QuestionService.java  
│   │   │   │   ├── AnswerService.java  
│   │   │   │   ├── TestTakerService.java  
│   │   │   │  
│   │   │   ├── config/  
│   │   │   │   ├── SecurityConfig.java  
│   │   │   │   ├── JwtAuthenticationFilter.java  
│   │   │   │   ├── JwtUtil.java  
│   │   │   │  
│   ├── resources/  
│   │   ├── static/  
│   │   │   ├── index.html  (Login Page)  
│   │   │   ├── admin-dashboard.html  
│   │   │   ├── test-page.html  
│   │   │   ├── results.html  
│   │   │   ├── css/  
│   │   │   │   ├── styles.css  
│   │   │   │  
│   │   │   ├── js/  
│   │   │   │   ├── auth.js  
│   │   │   │   ├── test.js  
│   │   │   │   ├── admin.js  
│   │   │   │  
│   │   ├── application.properties  
│  
│── pom.xml (Maven Dependencies)  
│── README.md  







**APIs/Endpoints:**
1. Endpoint to register a new user
POST: http://localhost:8080/api/users/register
Json Body: {
  "username": "supragya",
  "password": "practo",
  "role": "ROLE_TEST_TAKER"
}
2. Endpoint to login
POST: http://localhost:8080/auth/login
Json Body: 
{
  "username": "mayank",
  "password": "practo"
}
3. Endpoint to create test
POST: http://localhost:8080/api/tests
Json Body:{
  "name": "Practo Internship 11",
  "description": "Practo Internship Exam 2",
  "startTime": "2025-02-10 10:00:00",
  "endTime": "2025-02-10 12:00:00",
  "active": true
}
4. Endpoint to see all the created tests
GET: http://localhost:8080/api/tests/my-tests
5. Endpoint to create questions
POST: http://localhost:8080/api/questions/create
Json Body:
{
    "questionText": "What is JavaScript in CS?",
    "option1": "A language",
    "option2": "Fruit",
    "option3": "Game",
    "option4": "Scripting language",
    "correctOptionIndex": 4
}
6. Endpoint to get all the questions created
GET: http://localhost:8080/api/questions/my-questions

8. Endpoint to add questions to test
POST: http://localhost:8080/api/tests/3/add-questions
Json Body:
{
  "questionIds": [1, 2, 3, 4]
}

9. Endpoint to get questions by testId
GET: http://localhost:8080/api/tests/3/questions

10. Endpoint to assign tests to users
PUT: http://localhost:8080/api/tests/1/assign/2

11. Endpoint to fetch all tests by test_takers
GET: http://localhost:8080/api/test-takers/2/tests

12. Endpoint to fetch active tests for test_taker
GET: http://localhost:8080/api/test-takers/2/ongoing-test

13. Endpoint to start test by user
GET: http://localhost:8080/api/test-takers/2/start-test/1

14. Endpoint to submit test
POST: http://localhost:8080/api/test-takers/2/submit-test/1
Json Body: [
  {
    "question": { "id": 1 },
    "selectedOption": 0
  },
  {
    "question": { "id": 2 },
    "selectedOption": 0
  },
  {
    "question": { "id": 3 },
    "selectedOption": 0
  },
  {
    "question": { "id": 4 },
    "selectedOption": 0
  }
]


