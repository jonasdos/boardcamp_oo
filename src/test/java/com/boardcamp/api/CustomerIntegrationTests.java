package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;


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

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)


class CustomerIntegrationTests {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private CustomerRepository customerRepository;

  @BeforeEach
  void setup() {
    customerRepository.deleteAll();
  }
  @Test 
  void givenNewCustomer_whenCreatingCostumer_thenReturnCreatedCustomer() {
  //given
  CustomerDTO customerDTO = new CustomerDTO("Teste1", "1234567891","12345678910");
  HttpEntity<CustomerDTO> body = new HttpEntity<>(customerDTO);
  //when
  ResponseEntity<String> response = testRestTemplate.exchange(
    "/customers",
    HttpMethod.POST,
    body,
    String.class
  );


  //then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(customerDTO.getName(), customerRepository.findByName(customerDTO.getName()).get().getName());

  }
  @Test
  void givenCreateCustomer_whenCustomerExists_thenReturnConflict() {
    //given
    CustomerDTO customerDTO = new CustomerDTO("Teste1", "1234567891","12345678910");
    HttpEntity<CustomerDTO> body = new HttpEntity<>(customerDTO);
    //when
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/customers",
      HttpMethod.POST,
      body,
      String.class
    );
    ResponseEntity<String> response2 = testRestTemplate.exchange(
      "/customers",
      HttpMethod.POST,
      body,
      String.class
    );
    //then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(customerDTO.getName(), customerRepository.findByName(customerDTO.getName()).get().getName());
    assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    assertEquals("Customer's cpf is already registered", response2.getBody());
  }
  @Test
  void givenCustomers_whenGettingAllCustomers_thenReturnAllCustomers() {
    //given
    CustomerDTO customerDTO = new CustomerDTO("Teste1", "1234567891","12345678910");
    CustomerDTO customerDTO2 = new CustomerDTO("Teste2", "1234567892","12345678911");
    HttpEntity<CustomerDTO> body = new HttpEntity<>(customerDTO); 
    HttpEntity<CustomerDTO> body2 = new HttpEntity<>(customerDTO2);
    //when
    ResponseEntity<String> response = testRestTemplate.exchange(
      "/customers",
      HttpMethod.POST,
      body,
      String.class
    );
    ResponseEntity<String> response2 = testRestTemplate.exchange(
      "/customers",
      HttpMethod.POST,
      body2,
      String.class
    );
    ResponseEntity<String> response3 = testRestTemplate.exchange(
      "/customers",
      HttpMethod.GET,
      null,
      String.class
    );
    //then
    List<CustomerModel> customers = customerRepository.findAll();
    assertEquals(2, customers.size());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(customerDTO.getName(), customerRepository.findByName(customerDTO.getName()).get().getName());
    assertEquals(HttpStatus.CREATED, response2.getStatusCode());
    assertEquals(customerDTO2.getName(), customerRepository.findByName(customerDTO2.getName()).get().getName());
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    
}
}