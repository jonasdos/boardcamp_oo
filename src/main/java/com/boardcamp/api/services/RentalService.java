package com.boardcamp.api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.errors.NotFoundError;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.repositories.RentalRepository;

@Service
public class RentalService {
  final RentalRepository rentalRepository;
  RentalService(RentalRepository rentalRepository) {
    this.rentalRepository = rentalRepository;
  }
 
  public RentalModel createRental(RentalDTO rental, Optional<CustomerModel> customer, Optional<GameModel> game) {
    RentalModel newRental = new RentalModel(rental, customer, game);
    return rentalRepository.save(newRental);
  }
  
  public RentalModel returnRental(Long id) {
    Optional<RentalModel> rental = findRentalById(id);
    rental.get().setReturnDate(LocalDate.now());
    rental.get().setDelayFee(rental.get().calculateDelayFee());
    return rentalRepository.save(rental.get());
  }
  
  public List<RentalModel> getAllRentals() {
    return rentalRepository.findAll();
  }

  public Optional<RentalModel> findRentalById(Long id) {
    Optional<RentalModel> rental = rentalRepository.findById(id);
    if(rental.isEmpty()) {
      throw new NotFoundError("There is no rental with id: ");
    }
    return rental;
  }

  public void deleteRental(Long id) {
     
    rentalRepository.deleteById(id);
  }

}
