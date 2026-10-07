package com.subculture.backend.quiz.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 질문 순서이자 API 에서 쓰는 questionId. */
    @Column(nullable = false, unique = true)
    private int seq;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("seq")
    @BatchSize(size = 50)
    private List<Choice> choices = new ArrayList<>();

    public Question(int seq, String text) {
        this.seq = seq;
        this.text = text;
    }

    public Choice addChoice(String code, int seq, String text) {
        Choice choice = new Choice(this, code, seq, text);
        choices.add(choice);
        return choice;
    }
}
