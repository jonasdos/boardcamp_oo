package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.errors.NotFoundError;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;

@Service
public class CustomerService {
  final CustomerRepository customerRepository;
  CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }
  public List<CustomerModel> findAllCustomers() {
    return customerRepository.findAll();
  }
  
  public Optional<CustomerModel> findCustomerById(Long id) {

    return customerRepository.findById(id);
  }
  
  public Optional<CustomerModel> findCustomerByCpf(String cpf) {
    return customerRepository.findByCpf(cpf);
  }
  public CustomerModel createCustomer(CustomerDTO customer) {
    CustomerModel newCustomer = new CustomerModel(customer);
    return customerRepository.save(newCustomer);
  }
  public CustomerModel updateCustomer(Long id, CustomerDTO customer) {
    Optional<CustomerModel> customerOptional = customerRepository.findById(id);
    if (customerOptional.isEmpty()) {
      throw new NotFoundError("Customer not found");
    }
    CustomerModel updatedCustomer = customerOptional.get();
    updatedCustomer.setName(customer.getName());
    updatedCustomer.setPhone(customer.getPhone());
    updatedCustomer.setCpf(customer.getCpf());
    return customerRepository.save(updatedCustomer);
  }
  public void deleteCustomer(Long id) {
    Optional<CustomerModel> customerOptional = customerRepository.findById(id);
    if (customerOptional.isEmpty()) {
      throw new NotFoundError("Customer not found");
    }
    customerRepository.deleteById(id);
  }
}
