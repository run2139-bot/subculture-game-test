package com.subculture.backend.quiz;

import java.util.List;
import java.util.Map;

/** API 요청/응답 DTO. 선택지 가중치는 클라이언트에 노출하지 않는다. */
public final class QuizDto {

    private QuizDto() {
    }

    public record ChoiceResponse(String id, String text) {
    }

    public record QuestionResponse(int id, String text, List<ChoiceResponse> choices) {
    }

    public record AnswerRequest(Integer questionId, String choiceId) {
    }

    public record ResultRequest(List<AnswerRequest> answers) {
    }

    public record GameResult(int rank, String slug, String title, String developer, String description,
                             String imageUrl, List<String> recommendPoints, int matchPercent) {
    }

    public record ResultResponse(Map<String, Integer> traitScores, List<GameResult> results) {
    }
}
