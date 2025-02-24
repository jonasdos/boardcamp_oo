package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.dtos.GameDTO;
import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.repositories.GameRepository;
import com.boardcamp.api.repositories.RentalRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class RentalIntegrationTests {
  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private RentalRepository rentalRepository;

  @BeforeEach
  void setup() {
    rentalRepository.deleteAll();
    gameRepository.deleteAll();
    customerRepository.deleteAll();
    
  }
  
  @Test
  void givenNewRental_whenCreatingRental_thenReturnCreatedRental() {
    // given
    GameDTO game = new GameDTO("game","game" , 10, 10 );
    CustomerDTO customer = new CustomerDTO("customer", "1234567891", "12345678912");
    GameModel newGame = new GameModel(game);
    GameModel createdGame = gameRepository.save(newGame);
    CustomerModel newCustomer = new CustomerModel(customer);
    CustomerModel createdCustomer = customerRepository.save(newCustomer);
    RentalDTO rental = new RentalDTO(createdGame.getId(), createdCustomer.getId(), 5);
    HttpEntity<RentalDTO> body = new HttpEntity<>(rental);
    // when
    
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.POST,
      body,
      String.class
    );

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  void givenNewRental_whenStockis0_thenReturnUnprocessableEntity() {
    // given
    GameDTO game = new GameDTO("game","game" , 1, 10 );
    CustomerDTO customer = new CustomerDTO("customer", "1234567891", "12345678912");
    GameModel newGame = new GameModel(game);
    GameModel createdGame = gameRepository.save(newGame);
    CustomerModel newCustomer = new CustomerModel(customer);
    CustomerModel createdCustomer = customerRepository.save(newCustomer);
    RentalDTO rental = new RentalDTO(createdGame.getId(), createdCustomer.getId(), 5);
    HttpEntity<RentalDTO> body = new HttpEntity<>(rental);
    // when
    
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.POST,
      body,
      String.class
    );
    ResponseEntity<String> response2 = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.POST,
      body,
      String.class
    );

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response2.getStatusCode());
  }
  @Test
  void givenRentals_whenReturningAllRentals_thenReturnAllRentals() {
    // given
    GameDTO game = new GameDTO("game","game" , 5, 10 );
    CustomerDTO customer = new CustomerDTO("customer", "1234567891", "12345678912");
    GameModel newGame = new GameModel(game);
    GameModel createdGame = gameRepository.save(newGame);
    CustomerModel newCustomer = new CustomerModel(customer);
    CustomerModel createdCustomer = customerRepository.save(newCustomer);
    RentalDTO rental = new RentalDTO(createdGame.getId(), createdCustomer.getId(), 5);
    HttpEntity<RentalDTO> body = new HttpEntity<>(rental);
    

    // when 
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.POST,
      body,
      String.class
    );
    ResponseEntity<String> response2 = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.POST,
      body,
      String.class
    );
    ResponseEntity<String> response3 = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.GET,
      null,
      String.class
    );
    List<RentalModel> rentals = testRestTemplate.exchange(
      "/rentals",
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<List<RentalModel>>() {}
    ).getBody();

    //then 
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(HttpStatus.CREATED, response2.getStatusCode());
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertEquals(2, rentals.size());
}

}
