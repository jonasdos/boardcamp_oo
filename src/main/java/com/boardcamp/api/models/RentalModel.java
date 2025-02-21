package com.boardcamp.api.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;

import com.boardcamp.api.dtos.RentalDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_rentals")
public class RentalModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  
  @Column
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate rentalDate;
  
  @Column
  @NotNull
  @Min(0)
  private int daysRented;
  
  @Column
  private LocalDate returnDate;
  
  @Column
  @NotNull
  private int originalPrice;
  
  @Column
  @NotNull
  private int delayFee;
  
  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerModel customer;

  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false)
  private GameModel game;

  public RentalModel(RentalDTO body, Optional<CustomerModel> customer, Optional<GameModel> game) {
   
    this.rentalDate = LocalDate.now();
    this.daysRented = body.getDaysRented();
    this.returnDate = null;
    this.originalPrice = daysRented * game.get().getPricePerDay();
    this.delayFee = 0;
    this.customer = customer.get();
    this.game = game.get();
  } 
  
public int calculateDelayFee() {
    if (returnDate == null) {
      return 0;
    }
    long daysLate = ChronoUnit.DAYS.between(rentalDate, returnDate) - daysRented;
    return Math.max(0, (int) daysLate * game.getPricePerDay());
  }
 
}
