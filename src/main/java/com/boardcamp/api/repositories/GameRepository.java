package com.boardcamp.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardcamp.api.models.GameModel;



public interface GameRepository extends JpaRepository<GameModel, Long> {
  Optional<GameModel> findByName(String name);
  Optional<GameModel> findByImage(String image);
} 