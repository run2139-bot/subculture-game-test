package com.subculture.backend.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@Component
public class QuizDataLoader {

    private final QuizData.QuestionFile questionFile;
    private final QuizData.GameFile gameFile;

    public QuizDataLoader(ObjectMapper objectMapper) {
        this.questionFile = read(objectMapper, "data/questions.json", QuizData.QuestionFile.class);
        this.gameFile = read(objectMapper, "data/games.json", QuizData.GameFile.class);
    }

    public QuizData.QuestionFile questions() {
        return questionFile;
    }

    public QuizData.GameFile games() {
        return gameFile;
    }

    private static <T> T read(ObjectMapper mapper, String path, Class<T> type) {
        try (InputStream in = new ClassPathResource(path).getInputStream()) {
            return mapper.readValue(in, type);
        } catch (IOException e) {
            throw new UncheckedIOException("데이터 파일을 읽을 수 없습니다: " + path, e);
        }
    }
}
