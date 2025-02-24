package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.RentalDTO;
import com.boardcamp.api.errors.NotFoundError;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.models.GameModel;
import com.boardcamp.api.models.RentalModel;
import com.boardcamp.api.repositories.RentalRepository;
import com.boardcamp.api.services.RentalService;

@SpringBootTest
class RentalUnitTest {
  @InjectMocks
  private RentalService rentalService;

  @Mock
  private RentalRepository rentalRepository;

  @Test
  void givenRentalDTO_whenCreateRental_thenReturnSavedRental() {
    //given
    RentalDTO rentalDTO = new RentalDTO(1L, 1L, 3);
    CustomerModel customer = new CustomerModel(1L, "customer", "1234567891", "12345678912");
    GameModel game = new GameModel(1L, "game", "game", 10, 10);
    RentalModel rental = new RentalModel(rentalDTO, Optional.of(customer), Optional.of(game));
    when(rentalRepository.save(any(RentalModel.class))).thenReturn(rental);

    //when
    RentalModel savedRental = rentalService.createRental(rentalDTO, Optional.of(customer), Optional.of(game));

    //then
    verify(rentalRepository, times(1)).save(any(RentalModel.class));
    assertEquals(rental, savedRental);
  }

  @Test
  void givenRentalId_whenReturnRental_thenUpdateReturnDateAndDelayFee() {
    //given
    RentalModel rental = new RentalModel();
    rental.setId(1L);
    rental.setDaysRented(3);
    rental.setDelayFee(0);
    rental.setOriginalPrice(15);
    rental.setRentalDate(LocalDate.of(2025, 1, 30));
    rental.setReturnDate(null);
    rental.setCustomer(new CustomerModel());
    rental.setGame(new GameModel());
    when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
    when(rentalRepository.save(any())).thenReturn(rental);

    //when
    RentalModel returnedRental = rentalService.returnRental(1L);

    //then
    verify(rentalRepository, times(1)).findById(1L);
    verify(rentalRepository, times(1)).save(rental);
    assertEquals(LocalDate.now(), returnedRental.getReturnDate());
    assertEquals(rental.calculateDelayFee(), returnedRental.getDelayFee());
  }

  @Test
  void givenRentalId_whenRentalExists_thenReturnRental() {
    //given
    RentalModel rental = new RentalModel();
    rental.setId(1L);
    when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

    //when
    Optional<RentalModel> foundRental = rentalService.findRentalById(1L);

    //then
    verify(rentalRepository, times(1)).findById(1L);
    assertEquals(1L, foundRental.get().getId());
  }

  @Test
  void givenRentalId_whenRentalDoesNotExist_thenThrowNotFoundError() {
    //given
    when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

    //when
    NotFoundError error = assertThrows(NotFoundError.class, () -> rentalService.findRentalById(1L));

    //then
    verify(rentalRepository, times(1)).findById(1L);
    assertEquals("There is no rental with id: ", error.getMessage());
  }

  @Test
  void givenRentals_whenGetAllRentals_thenReturnRentalList() {
    //given
    List<RentalModel> rentals = new ArrayList<>();
    rentals.add(new RentalModel());
    when(rentalRepository.findAll()).thenReturn(rentals);

    //when
    List<RentalModel> foundRentals = rentalService.getAllRentals();

    //then
    verify(rentalRepository, times(1)).findAll();
    assertEquals(1, foundRentals.size());
  }

  @Test
  void givenRentalId_whenDeleteRental_thenRemoveFromRepository() {
    //given
    RentalModel rental = new RentalModel();
    rental.setId(1L);
    when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

    //when
    rentalService.deleteRental(1L);

    //then
    verify(rentalRepository, times(1)).findById(1L);
    verify(rentalRepository, times(1)).deleteById(1L);
  }
}