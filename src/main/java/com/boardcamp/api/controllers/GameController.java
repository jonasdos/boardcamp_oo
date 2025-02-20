package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.services.GameService;

import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/games")
public class GameController {

  final GameService gameService;
  GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping()
  public ResponseEntity<Object> createGame(@RequestBody @Valid GameDTO body) {
    Optional<GameModel> game = gameService.findByName(body.getName());
    if (game.isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Game is already registered");
    }
    Optional<GameModel> gameimg = gameService.findByImage(body.getImage());
    if (gameimg.isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("The image is already registered an another game");
    }
    GameModel newGame = gameService.save(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(newGame);
  }

  @GetMapping
  public ResponseEntity<Object> getAllGames() {
    return ResponseEntity.status(HttpStatus.OK).body(gameService.findAll());
  }
  
}