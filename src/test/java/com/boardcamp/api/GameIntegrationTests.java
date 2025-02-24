package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.repositories.GameRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class GameIntegrationTests {
  
  @Autowired
  private TestRestTemplate testRestTemplate;
  
  @Autowired
  private GameRepository gameRepository;

  @BeforeEach
  void setup() {
    gameRepository.deleteAll();
  }

  @Test
  void givenNewGame_whenCreatingGame_thenReturnCreatedGame() {
    // given
    GameDTO newGame = new GameDTO("Teste", "imagem.png", 10, 10);
    HttpEntity<GameDTO> body = new HttpEntity<>(newGame);

    // when
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/games",
      HttpMethod.POST,
      body,
      String.class
    );

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(1, gameRepository.count());
  }

  @Test 
  void givenGame_whenGameExists_ThenThrowError() {
    // given
    GameDTO newGame = new GameDTO("Teste", "imagem.png", 10, 10);
    GameDTO newGame2 = new GameDTO("Teste", "imagem.png", 10, 10);
    HttpEntity<GameDTO> body = new HttpEntity<>(newGame);
    HttpEntity<GameDTO> body2 = new HttpEntity<>(newGame2);
    // when
    ResponseEntity<String> response = testRestTemplate.exchange(
    "/games",
    HttpMethod.POST,
    body,
    String.class
  );
  ResponseEntity<String> response2 = testRestTemplate.exchange(
    "/games",
    HttpMethod.POST,
    body2,
    String.class
  );

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    assertEquals(1, gameRepository.count());
  }

  @Test
  void givenMissingData_whenCreatingGame_thenReturnBadRequest() {
    // given
    GameDTO newGame = new GameDTO("Teste", "imagem.png", 0, 0);
    
    HttpEntity<Object> body = new HttpEntity<>(newGame);

    // when
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/games",
      HttpMethod.POST,
      body,
      String.class
    );

    // then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(0, gameRepository.count());
  }

  @Test 
  void givenGames_whenSearchingGames_thenReturnAllGames() {
    // given
    GameDTO newGame = new GameDTO("Teste", "imagem.png", 10, 10);
    GameDTO newGame2 = new GameDTO("Teste2", "imagem2.png", 10, 10);
    HttpEntity<GameDTO> body = new HttpEntity<>(newGame);
    HttpEntity<GameDTO> body2 = new HttpEntity<>(newGame2);
    // when
    ResponseEntity<String> response = testRestTemplate.exchange(
    "/games",
    HttpMethod.POST,
    body,
    String.class
  );
  ResponseEntity<String> response2 = testRestTemplate.exchange(
    "/games",
    HttpMethod.POST,
    body2,
    String.class
  );
  ResponseEntity<String> response3 = testRestTemplate.exchange(
    "/games",
    HttpMethod.GET,
    null,
    String.class
  );

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(HttpStatus.CREATED, response2.getStatusCode());
    assertEquals(2, gameRepository.count());
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertEquals(2, response3.getBody().split("id").length-1);
   
  }
}
