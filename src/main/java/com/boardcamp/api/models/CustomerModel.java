package com.boardcamp.api.models;

import com.boardcamp.api.dtos.CustomerDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb-customers")
public class CustomerModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column
  @NotBlank(message = "The name cannot be empty")
  private String name;
  @Column
  @NotBlank(message = "The phone number cannot be empty")
  private String phone;
  @Column
  @NotBlank(message = "The cpf cannot be empty") 
  @Size(min = 11, max = 11, message = "The cpf must have 11 characters") 
  private String cpf; 

  public CustomerModel(CustomerDTO customer) {
    this.name = customer.getName();
    this.phone = customer.getPhone();
    this.cpf = customer.getCpf();
  }
}
