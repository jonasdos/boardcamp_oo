package com.boardcamp.api.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GameDTO {
 @Column(unique = true, nullable = false, length = 50 )
  @NotBlank(message = "O nome do jogo não pode ser nulo")
  @Size(min = 2, max = 50, message = "O nome do jogo deve ter entre 2 e 50 caracteres")
  private String name;

  @Column(unique = true)
  @NotBlank(message = "A imagem do jogo não pode ser nula")
  private String image;

  @Column
  @NotNull(message = "O estoque total não pode ser nulo")
  @Min(value = 1, message = "O estoque total deve ser maior ou igual a 0")
  private int stockTotal;

  @Column
  @NotNull(message = "O preço por dia não pode ser nulo")
  @Min(value = 1, message = "O preço por dia deve ser maior ou igual a 0")
  private int pricePerDay;

}
