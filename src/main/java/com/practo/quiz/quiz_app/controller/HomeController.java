package com.practo.quiz.quiz_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index.html";  // Ensure this file is inside src/main/resources/static/
    }
}
