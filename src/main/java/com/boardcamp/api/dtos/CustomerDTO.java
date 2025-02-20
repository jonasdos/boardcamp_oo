package com.boardcamp.api.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
  @Column
  @NotBlank(message = "The name cannot be empty")
  private String name;
  @Column
  @Size(min = 10, max = 11, message = "The phone number must have 10 or 11 characters") 
  private String phone;
  @Column
  @NotBlank(message = "The cpf cannot be empty")
  @Size(min = 11, max = 11, message = "The cpf must have 11 characters") 
  private String cpf; 

}
