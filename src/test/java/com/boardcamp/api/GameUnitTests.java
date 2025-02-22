package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.errors.ConflictError;
import com.boardcamp.api.errors.NotFoundError;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.services.GameService;

@SpringBootTest
class GameUnitTests {
  @InjectMocks
  private GameService gameService;

  @Mock
  private GameRepository gameRepository;

  @Test
  void givenGameId_whenGamesExists_thenReturnTheGame() {
    //given
    GameModel game = new GameModel();
    game.setId(1L);
    game.setName("Test Game");    
    when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

    //when
    Optional<GameModel> foundGame = gameService.findById(1L);

    //then
    verify(gameRepository, times(1)).findById(any());
    verify(gameRepository, times(0)).existsById(any());
    assertEquals(1L, foundGame.get().getId());
    assertEquals("Test Game", foundGame.get().getName());

  }
  @Test
  void givenGameId_whenGamesNotExists_thenThowError() {
    //given
    GameModel game = new GameModel();
    game.setId(1L);
    game.setName("Test Game");    
    when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

    //when
    NotFoundError error = assertThrows(NotFoundError.class, () -> gameService.findById(2L));

    //then
    verify(gameRepository, times(1)).findById(any());
    verify(gameRepository, times(0)).existsById(any());
    assertEquals("Game id not found", error.getMessage());

  }

  @Test
  void givenGameDTO_whenGameNameExists_thenThrowConflictError() {
    //given
    GameDTO gameDTO = new GameDTO();
    gameDTO.setName("Test Game");
    when(gameRepository.findByName("Test Game")).thenReturn(Optional.of(new GameModel(gameDTO)));

    //when
    ConflictError error = assertThrows(ConflictError.class, () -> gameService.save(gameDTO));

    //then
    verify(gameRepository, times(1)).findByName("Test Game");
    assertEquals("Game is already registered", error.getMessage());
  }

  @Test
  void givenGameDTO_whenGameImageExists_thenThrowConflictError() {
    //given
    GameDTO gameDTO = new GameDTO();
    gameDTO.setImage("test_image.png");
    when(gameRepository.findByImage("test_image.png")).thenReturn(Optional.of(new GameModel()));

    //when
    ConflictError error = assertThrows(ConflictError.class, () -> gameService.save(gameDTO));

    //then
    verify(gameRepository, times(1)).findByImage("test_image.png");
    assertEquals("The image is already registered an another game", error.getMessage());
  }
  @Test
  void givenGameDTO_whenGameDoesNotExist_thenSaveGame() {
    //given
    GameDTO gameDTO = new GameDTO();
    gameDTO.setName("New Game");
    gameDTO.setImage("new_image.png");
    when(gameRepository.findByName("New Game")).thenReturn(Optional.empty());
    when(gameRepository.findByImage("new_image.png")).thenReturn(Optional.empty());
    when(gameRepository.save(any(GameModel.class))).thenReturn(new GameModel(gameDTO));

    //when
    GameModel savedGame = gameService.save(gameDTO);

    //then
    verify(gameRepository, times(1)).findByName("New Game");
    verify(gameRepository, times(1)).findByImage("new_image.png");
    verify(gameRepository, times(1)).save(any(GameModel.class));
    assertEquals("New Game", savedGame.getName());
    assertEquals("new_image.png", savedGame.getImage());
  }

  


}