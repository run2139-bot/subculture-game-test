package com.subculture.backend.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"choice_id", "trait_code"}))
public class ChoiceWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Choice choice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trait_code")
    private Trait trait;

    /** 음수 허용. */
    @Column(nullable = false)
    private int weight;

    ChoiceWeight(Choice choice, Trait trait, int weight) {
        this.choice = choice;
        this.trait = trait;
        this.weight = weight;
    }
}
