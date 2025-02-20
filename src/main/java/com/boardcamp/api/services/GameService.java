package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.errors.NotFoundError;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;

@Service
public class GameService {

  final GameRepository gameRepository;

  GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public List<GameModel> findAll() {
    List<GameModel> games = gameRepository.findAll();
    if (games.isEmpty()) {
      throw new NotFoundError("Game list is empty");
    }
    return games;
  }

  public Optional<GameModel> findById(Long id) {
    Optional<GameModel> game = gameRepository.findById(id);
    if (game.isEmpty()) {
      throw new NotFoundError("Game id not found");
    }
    return game;
  }

  public Optional<GameModel> findByName(String name) {
    return gameRepository.findByName(name);
  }
  public Optional<GameModel> findByImage(String image) {
    return gameRepository.findByImage(image);
  }

  public GameModel save(GameDTO body) {
    GameModel game = new GameModel(body);
    return gameRepository.save(game);
  }

  public GameModel update(Long id, GameModel game) {
    if (gameRepository.existsById(id)) {
      game.setId(id);
      return gameRepository.save(game);
    } else {
      throw new NotFoundError("Game not found");
    }
  }

  public void delete(Long id) {
    if (gameRepository.existsById(id)) {
      gameRepository.deleteById(id);
    } else {
      throw new NotFoundError("Game not found");
    }
  }
}
