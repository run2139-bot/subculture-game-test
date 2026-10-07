package com.subculture.backend.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trait {

    @Id
    @Column(length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int seq;

    public Trait(String code, String name, int seq) {
        this.code = code;
        this.name = name;
        this.seq = seq;
    }
}
