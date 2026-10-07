package com.subculture.backend.quiz;

import com.subculture.backend.quiz.QuizDto.QuestionResponse;
import com.subculture.backend.quiz.QuizDto.ResultRequest;
import com.subculture.backend.quiz.QuizDto.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/questions")
    public List<QuestionResponse> questions() {
        return quizService.getQuestions();
    }

    @PostMapping("/results")
    public ResultResponse results(@RequestBody ResultRequest request) {
        return quizService.calculate(request);
    }
}
