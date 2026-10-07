package com.subculture.backend.quiz;

import java.util.List;
import java.util.Map;

/** classpath:data/*.json 을 읽어 온 원본 데이터 모델. */
public final class QuizData {

    private QuizData() {
    }

    public record Choice(String id, String text, Map<String, Integer> weights) {
    }

    public record Question(int id, String text, List<Choice> choices) {
    }

    public record QuestionFile(Map<String, String> traits, List<Question> questions) {
    }

    public record Game(String slug, String title, String developer, String description,
                       String imageUrl, List<String> recommendPoints, Map<String, Integer> profile) {
    }

    public record GameFile(List<Game> games) {
    }
}
