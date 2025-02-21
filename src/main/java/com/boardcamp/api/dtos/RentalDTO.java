package com.boardcamp.api.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalDTO {
  @Column
  @NotNull
  private Long customerId;
  
  @Column
  @NotNull
  private Long gameId;

  @Column
  @NotNull
  @Min(1)
  private int daysRented;
}
