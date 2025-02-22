package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.errors.ConflictError;
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
    return gameRepository.findAll();
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
    if(gameRepository.findByName(body.getName()).isPresent()) {
      throw new ConflictError("Game is already registered");
    }
    if(gameRepository.findByImage(body.getImage()).isPresent()) {
      throw new ConflictError("The image is already registered an another game");
    }
    GameModel game = new GameModel(body);
    return gameRepository.save(game);
  }

  public boolean stockAvailable(Long id) {
    Optional<GameModel> game = gameRepository.findById(id);
    if (game.isEmpty()) {
      throw new NotFoundError("Game id not found");
    }
    return game.get().getStockTotal() > 0;
  }

  public void rentGame(Long id) {
    Optional<GameModel> game = gameRepository.findById(id);
    if (game.isEmpty()) {
      throw new NotFoundError("Game id not found");
    }
    game.get().setStockTotal(game.get().getStockTotal() - 1);
    gameRepository.save(game.get());
  }

  public void returnGame(Long id) {
    Optional<GameModel> game = gameRepository.findById(id);
    if (game.isEmpty()) {
      throw new NotFoundError("Game id not found");
    }
    game.get().setStockTotal(game.get().getStockTotal() + 1);
  
    gameRepository.save(game.get());
  }


  public void delete(Long id) {
    if (gameRepository.existsById(id)) {
      gameRepository.deleteById(id);
    } else {
      throw new NotFoundError("Game already deleted");
    }
  }
}
