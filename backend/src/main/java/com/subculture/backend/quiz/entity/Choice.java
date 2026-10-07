package com.subculture.backend.quiz.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;

    /** API 에서 쓰는 choiceId (예: "1A"). */
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false)
    private int seq;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "choice", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private List<ChoiceWeight> weights = new ArrayList<>();

    Choice(Question question, String code, int seq, String text) {
        this.question = question;
        this.code = code;
        this.seq = seq;
        this.text = text;
    }

    public void addWeight(Trait trait, int weight) {
        weights.add(new ChoiceWeight(this, trait, weight));
    }
}
