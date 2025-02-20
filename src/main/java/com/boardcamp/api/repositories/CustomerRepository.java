package com.boardcamp.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardcamp.api.models.CustomerModel;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
  Optional<CustomerModel> findByCpf(String cpf);
  Optional<CustomerModel> findByPhone(String phone);
  Optional<CustomerModel> findByName(String name);

  
} 