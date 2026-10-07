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
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    private String developer;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private List<GameProfile> profile = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("seq")
    @BatchSize(size = 50)
    private List<RecommendPoint> recommendPoints = new ArrayList<>();

    public Game(String slug, String title, String developer, String description, String imageUrl) {
        this.slug = slug;
        this.title = title;
        this.developer = developer;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void addProfile(Trait trait, int score) {
        profile.add(new GameProfile(this, trait, score));
    }

    public void addRecommendPoint(int seq, String content) {
        recommendPoints.add(new RecommendPoint(this, seq, content));
    }
}
