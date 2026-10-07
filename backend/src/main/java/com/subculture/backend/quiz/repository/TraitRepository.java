package com.subculture.backend.quiz.repository;

import com.subculture.backend.quiz.entity.Trait;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TraitRepository extends JpaRepository<Trait, String> {

    List<Trait> findAllByOrderBySeqAsc();
}
