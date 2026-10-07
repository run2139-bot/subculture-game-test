package com.subculture.backend.quiz;

import com.subculture.backend.quiz.entity.Choice;
import com.subculture.backend.quiz.entity.Game;
import com.subculture.backend.quiz.entity.Question;
import com.subculture.backend.quiz.entity.Trait;
import com.subculture.backend.quiz.repository.GameRepository;
import com.subculture.backend.quiz.repository.QuestionRepository;
import com.subculture.backend.quiz.repository.TraitRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 테이블이 비어 있을 때만 classpath:data/*.json 을 DB 에 적재한다.
 * 이미 데이터가 있으면 건드리지 않으므로, JSON 수정 사항을 반영하려면 해당 테이블을 비우고 재시작해야 한다.
 */
@Component
public class QuizDataSeeder implements ApplicationRunner {

    private final QuizDataLoader loader;
    private final TraitRepository traitRepository;
    private final QuestionRepository questionRepository;
    private final GameRepository gameRepository;

    public QuizDataSeeder(QuizDataLoader loader, TraitRepository traitRepository,
                          QuestionRepository questionRepository, GameRepository gameRepository) {
        this.loader = loader;
        this.traitRepository = traitRepository;
        this.questionRepository = questionRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Map<String, Trait> traits = seedTraits();
        if (questionRepository.count() == 0) {
            seedQuestions(traits);
        }
        if (gameRepository.count() == 0) {
            seedGames(traits);
        }
    }

    private Map<String, Trait> seedTraits() {
        Map<String, Trait> result = new LinkedHashMap<>();
        if (traitRepository.count() == 0) {
            int seq = 0;
            for (Map.Entry<String, String> e : loader.questions().traits().entrySet()) {
                result.put(e.getKey(), traitRepository.save(new Trait(e.getKey(), e.getValue(), seq++)));
            }
        } else {
            traitRepository.findAllByOrderBySeqAsc().forEach(t -> result.put(t.getCode(), t));
        }
        return result;
    }

    private void seedQuestions(Map<String, Trait> traits) {
        for (QuizData.Question q : loader.questions().questions()) {
            Question question = new Question(q.id(), q.text());
            int seq = 0;
            for (QuizData.Choice c : q.choices()) {
                Choice choice = question.addChoice(c.id(), seq++, c.text());
                c.weights().forEach((code, weight) -> choice.addWeight(trait(traits, code), weight));
            }
            questionRepository.save(question);
        }
    }

    private void seedGames(Map<String, Trait> traits) {
        for (QuizData.Game g : loader.games().games()) {
            Game game = new Game(g.slug(), g.title(), g.developer(), g.description(), g.imageUrl());
            g.profile().forEach((code, score) -> game.addProfile(trait(traits, code), score));
            List<String> points = g.recommendPoints();
            for (int i = 0; i < points.size(); i++) {
                game.addRecommendPoint(i, points.get(i));
            }
            gameRepository.save(game);
        }
    }

    private static Trait trait(Map<String, Trait> traits, String code) {
        Trait trait = traits.get(code);
        if (trait == null) {
            throw new IllegalStateException("정의되지 않은 성향 코드: " + code);
        }
        return trait;
    }
}
