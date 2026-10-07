package com.subculture.backend.quiz.repository;

import com.subculture.backend.quiz.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
