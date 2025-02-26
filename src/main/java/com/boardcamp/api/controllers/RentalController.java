package com.boardcamp.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.services.CustomerService;
import com.boardcamp.api.services.GameService;
import com.boardcamp.api.services.RentalService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
public class RentalController {
  
  final RentalService rentalService;
  final CustomerService customerService;
  final GameService gameService;
  RentalController(RentalService rentalService, CustomerService customerService, GameService gameService) {
    this.rentalService = rentalService;
    this.customerService = customerService;
    this.gameService = gameService;
  } 
 


@PostMapping()
public ResponseEntity<Object> createRental(@RequestBody @Valid RentalDTO body) {

  Optional<CustomerModel> customer = customerService.findCustomerById(body.getCustomerId());
  Optional<GameModel> game = gameService.findById(body.getGameId());
  boolean stockAvailable = gameService.stockAvailable(body.getGameId());
  if (!stockAvailable) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Game out of stock");
  }
  gameService.rentGame(body.getGameId());
  RentalModel createdRental = rentalService.createRental(body, customer, game);

    
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRental);
}

@PostMapping("/{id}/return")
public ResponseEntity<Object> returnRental(@PathVariable Long id) {
  Optional<RentalModel> rental = rentalService.findRentalById(id);
  if (rental.get().getReturnDate() != null) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Rental already returned");
  }
  gameService.returnGame(rental.get().getGame().getId());
  RentalModel finishedRental = rentalService.returnRental(id);
  return ResponseEntity.status(HttpStatus.OK).body(finishedRental);
}


@GetMapping()
public ResponseEntity<Object> getAllRentals() {
  List<RentalModel> rentals = rentalService.getAllRentals();
  return ResponseEntity.status(HttpStatus.OK).body(rentals);
}

@DeleteMapping({"/{id}"})
public ResponseEntity<Object> deleteRental(@PathVariable Long id) {
  Optional<RentalModel> rental = rentalService.findRentalById(id);
  if(rental.get().getReturnDate() == null) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rental not returned yet");
  }
  rentalService.deleteRental(id);
  return ResponseEntity.status(HttpStatus.OK).body(null);
}

}
