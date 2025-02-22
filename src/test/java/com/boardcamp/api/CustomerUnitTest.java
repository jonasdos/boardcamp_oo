package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomerDTO;
import com.boardcamp.api.errors.NotFoundError;
import com.boardcamp.api.models.CustomerModel;
import com.boardcamp.api.repositories.CustomerRepository;
import com.boardcamp.api.services.CustomerService;

@SpringBootTest
class CustomerUnitTest {
  @InjectMocks
  private CustomerService customerService;

  @Mock
  private CustomerRepository customerRepository;

  @Test
  void givenCustomers_whenFindAllCustomers_thenReturnCustomerList() {
    //given
    List<CustomerModel> customers = new ArrayList<>();
    customers.add(new CustomerModel());
    when(customerRepository.findAll()).thenReturn(customers);

    //when
    List<CustomerModel> foundCustomers = customerService.findAllCustomers();

    //then
    verify(customerRepository, times(1)).findAll();
    assertEquals(1, foundCustomers.size());
  }

  @Test
  void givenCustomerId_whenCustomerExists_thenReturnCustomer() {
    //given
    CustomerModel customer = new CustomerModel();
    customer.setId(1L);
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    //when
    Optional<CustomerModel> foundCustomer = customerService.findCustomerById(1L);

    //then
    verify(customerRepository, times(1)).findById(1L);
    assertEquals(1L, foundCustomer.get().getId());
  }

  @Test
  void givenCustomerId_whenCustomerDoesNotExist_thenThrowNotFoundError() {
    //given
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    //when
    NotFoundError error = assertThrows(NotFoundError.class, () -> customerService.findCustomerById(1L));

    //then
    verify(customerRepository, times(1)).findById(1L);
    assertEquals("Customer not found", error.getMessage());
  }

  @Test
  void givenCustomerCpf_whenCustomerExists_thenReturnCustomer() {
    //given
    CustomerModel customer = new CustomerModel();
    customer.setCpf("12345678900");
    when(customerRepository.findByCpf("12345678900")).thenReturn(Optional.of(customer));

    //when
    Optional<CustomerModel> foundCustomer = customerService.findCustomerByCpf("12345678900");

    //then
    verify(customerRepository, times(1)).findByCpf("12345678900");
    assertEquals("12345678900", foundCustomer.get().getCpf());
  }

  @Test
  void givenCustomerDTO_whenCreateCustomer_thenReturnSavedCustomer() {
    //given
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setName("John Doe");
    customerDTO.setPhone("123456789");
    customerDTO.setCpf("12345678900");
    CustomerModel customer = new CustomerModel(customerDTO);
    when(customerRepository.save(any(CustomerModel.class))).thenReturn(customer);

    //when
    CustomerModel savedCustomer = customerService.createCustomer(customerDTO);

    //then
    verify(customerRepository, times(1)).save(any(CustomerModel.class));
    assertEquals("John Doe", savedCustomer.getName());
    assertEquals("123456789", savedCustomer.getPhone());
    assertEquals("12345678900", savedCustomer.getCpf());
  }

  @Test
  void givenCustomerIdAndDTO_whenUpdateCustomer_thenReturnUpdatedCustomer() {
    //given
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setName("Jane Doe");
    customerDTO.setPhone("987654321");
    customerDTO.setCpf("09876543210");
    CustomerModel customer = new CustomerModel();
    customer.setId(1L);
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(customerRepository.save(any(CustomerModel.class))).thenReturn(customer);

    //when
    CustomerModel updatedCustomer = customerService.updateCustomer(1L, customerDTO);

    //then
    verify(customerRepository, times(1)).findById(1L);
    verify(customerRepository, times(1)).save(any(CustomerModel.class));
    assertEquals("Jane Doe", updatedCustomer.getName());
    assertEquals("987654321", updatedCustomer.getPhone());
    assertEquals("09876543210", updatedCustomer.getCpf());
  }

  @Test
  void givenCustomerId_whenDeleteCustomer_thenRemoveFromRepository() {
    //given
    CustomerModel customer = new CustomerModel();
    customer.setId(1L);
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    //when
    customerService.deleteCustomer(1L);

    //then
    verify(customerRepository, times(1)).findById(1L);
    verify(customerRepository, times(1)).deleteById(1L);
  }

  @Test
  void givenCustomerId_whenDeleteNonExistingCustomer_thenThrowNotFoundError() {
    //given
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    //when
    NotFoundError error = assertThrows(NotFoundError.class, () -> customerService.deleteCustomer(1L));

    //then
    verify(customerRepository, times(1)).findById(1L);
    verify(customerRepository, times(0)).deleteById(1L);
    assertEquals("Customer not found", error.getMessage());
  }
}