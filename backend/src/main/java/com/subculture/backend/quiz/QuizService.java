package com.subculture.backend.quiz;

import com.subculture.backend.quiz.QuizDto.AnswerRequest;
import com.subculture.backend.quiz.QuizDto.ChoiceResponse;
import com.subculture.backend.quiz.QuizDto.GameResult;
import com.subculture.backend.quiz.QuizDto.QuestionResponse;
import com.subculture.backend.quiz.QuizDto.ResultRequest;
import com.subculture.backend.quiz.QuizDto.ResultResponse;
import com.subculture.backend.quiz.entity.Choice;
import com.subculture.backend.quiz.entity.Game;
import com.subculture.backend.quiz.entity.GameProfile;
import com.subculture.backend.quiz.entity.Question;
import com.subculture.backend.quiz.entity.RecommendPoint;
import com.subculture.backend.quiz.entity.Trait;
import com.subculture.backend.quiz.repository.GameRepository;
import com.subculture.backend.quiz.repository.QuestionRepository;
import com.subculture.backend.quiz.repository.TraitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuizService {

    private static final int RESULT_LIMIT = 5;

    private final TraitRepository traitRepository;
    private final QuestionRepository questionRepository;
    private final GameRepository gameRepository;

    public QuizService(TraitRepository traitRepository, QuestionRepository questionRepository,
                       GameRepository gameRepository) {
        this.traitRepository = traitRepository;
        this.questionRepository = questionRepository;
        this.gameRepository = gameRepository;
    }

    public List<QuestionResponse> getQuestions() {
        return questionRepository.findAllByOrderBySeqAsc().stream()
                .map(q -> new QuestionResponse(q.getSeq(), q.getText(),
                        q.getChoices().stream().map(c -> new ChoiceResponse(c.getCode(), c.getText())).toList()))
                .toList();
    }

    public ResultResponse calculate(ResultRequest request) {
        List<String> traitCodes = traitRepository.findAllByOrderBySeqAsc().stream().map(Trait::getCode).toList();
        Map<String, Integer> scores = sumTraitScores(traitCodes, validate(request));
        double[] user = toVector(traitCodes, scores);

        List<GameResult> ranked = new ArrayList<>();
        gameRepository.findAll().stream()
                .map(g -> Map.entry(g, cosine(user, toVector(traitCodes, profileOf(g)))))
                .sorted(Map.Entry.<Game, Double>comparingByValue(Comparator.reverseOrder()))
                .limit(RESULT_LIMIT)
                .forEach(e -> {
                    Game g = e.getKey();
                    ranked.add(new GameResult(ranked.size() + 1, g.getSlug(), g.getTitle(), g.getDeveloper(),
                            g.getDescription(), g.getImageUrl(),
                            g.getRecommendPoints().stream().map(RecommendPoint::getContent).toList(),
                            (int) Math.round(e.getValue() * 100)));
                });
        return new ResultResponse(scores, ranked);
    }

    /** 모든 질문에 한 번씩, 유효한 선택지로 답했는지 검증하고 선택된 Choice 목록을 반환한다. */
    private List<Choice> validate(ResultRequest request) {
        if (request == null || request.answers() == null) {
            throw badRequest("answers 는 필수입니다.");
        }
        Map<Integer, Question> questions = questionRepository.findAllByOrderBySeqAsc().stream()
                .collect(Collectors.toMap(Question::getSeq, Function.identity()));

        List<Choice> selected = new ArrayList<>();
        Set<Integer> answered = new HashSet<>();
        for (AnswerRequest a : request.answers()) {
            if (a == null || a.questionId() == null || a.choiceId() == null) {
                throw badRequest("questionId 와 choiceId 는 필수입니다.");
            }
            Question q = questions.get(a.questionId());
            if (q == null) {
                throw badRequest("존재하지 않는 질문입니다: " + a.questionId());
            }
            if (!answered.add(q.getSeq())) {
                throw badRequest("중복 응답된 질문입니다: " + q.getSeq());
            }
            selected.add(q.getChoices().stream()
                    .filter(c -> c.getCode().equals(a.choiceId()))
                    .findFirst()
                    .orElseThrow(() -> badRequest("질문 " + q.getSeq() + " 에 없는 선택지입니다: " + a.choiceId())));
        }
        if (answered.size() != questions.size()) {
            throw badRequest("모든 질문(" + questions.size() + "개)에 응답해야 합니다.");
        }
        return selected;
    }

    private Map<String, Integer> sumTraitScores(List<String> traitCodes, List<Choice> selected) {
        Map<String, Integer> scores = new LinkedHashMap<>();
        traitCodes.forEach(t -> scores.put(t, 0));
        selected.forEach(c -> c.getWeights()
                .forEach(w -> scores.merge(w.getTrait().getCode(), w.getWeight(), Integer::sum)));
        return scores;
    }

    private static Map<String, Integer> profileOf(Game game) {
        return game.getProfile().stream()
                .collect(Collectors.toMap(p -> p.getTrait().getCode(), GameProfile::getScore));
    }

    /** 음수는 0으로 보정하고, 전부 0이면 균등 벡터를 사용한다. */
    private static double[] toVector(List<String> traitCodes, Map<String, Integer> byTrait) {
        double[] v = new double[traitCodes.size()];
        double sum = 0;
        for (int i = 0; i < v.length; i++) {
            v[i] = Math.max(0, byTrait.getOrDefault(traitCodes.get(i), 0));
            sum += v[i];
        }
        if (sum == 0) {
            Arrays.fill(v, 1.0);
        }
        return v;
    }

    private static double cosine(double[] a, double[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return (na == 0 || nb == 0) ? 0 : dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
