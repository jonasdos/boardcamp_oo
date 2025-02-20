package com.boardcamp.api.models;

import com.boardcamp.api.dtos.GameDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_games")
public class GameModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true, nullable = false, length = 50 )
  @NotBlank(message = "O nome do jogo não pode ser nulo")
  @Size(min = 2, max = 50, message = "O nome do jogo deve ter entre 2 e 50 caracteres")
  private String name;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "A imagem do jogo não pode ser nula")
  private String image;

  @Column
  @NotNull(message = "O estoque total não pode ser nulo")
  @Min(value = 0, message = "O estoque total deve ser maior ou igual a 0")
  private int stockTotal;

  @Column
  @NotNull(message = "O preço por dia não pode ser nulo")
  @Min(value = 0, message = "O preço por dia deve ser maior ou igual a 0")
  private int pricePerDay;
  
  public GameModel(GameDTO body) {
    this.name = body.getName();
    this.image = body.getImage();
    this.stockTotal = body.getStockTotal();
    this.pricePerDay = body.getPricePerDay();
  }
}