package com.boardcamp.api.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  
  final CustomerService customerService;
  CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }
  
  @PostMapping()
  public ResponseEntity<Object> createCustomer(@RequestBody @Valid CustomerDTO body) {
    Optional<CustomerModel> cpfVerify = customerService.findCustomerByCpf(body.getCpf());
    
    if(cpfVerify.isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer's cpf is already registered");
    }
    CustomerModel newCustomer = customerService.createCustomer(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
  }
  
  @GetMapping()
  public ResponseEntity<Object> getAllCustomers() {
    return ResponseEntity.status(HttpStatus.OK).body(customerService.findAllCustomers());
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Object> getCustomerById(@PathVariable Long id) {
    Optional<CustomerModel> customer = customerService.findCustomerById(id);
    if (customer.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(customer.get());
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerDTO body) {
    CustomerModel updatedCustomer = customerService.updateCustomer(id, body);
    return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
    Optional<CustomerModel> customer = customerService.findCustomerById(id);
    if (customer.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
    }
    customerService.deleteCustomer(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
  
  
}
