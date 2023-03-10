package com.example.TestingProject.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void saveCustomer() {
        Customer customer = new Customer("Abel", "+1234");
        customerRepository.save(customer);
        UUID id = customer.getId();

        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo(customer.getName());
//                    assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
                    assertThat(c.equals(customer)).isTrue();
                });
    }

    @Test
    void selectCustomerByPhoneNumber() {
        String phoneNumber = "+1234";
        Customer customer = new Customer("Abel", phoneNumber);
        customerRepository.save(customer);
        Optional<Customer> optionalCustomer = customerRepository.selectByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.equals(customer)).isTrue();
                });

    }

    @Test
    void notSelectCustomerByPhoneNumberWhenNumberDoesNotExist() {
        String phoneNumber = "+1234";
        Customer customer = new Customer("Abel", phoneNumber);
        customerRepository.save(customer);
        Optional<Customer> optionalCustomer = customerRepository.selectByPhoneNumber("+0000");
        assertThat(optionalCustomer)
                .isNotPresent();
    }

    @Test
    void notSaveCustomerWhenNameIsNull() {
        Customer customer = new Customer(null, "+1234");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();

    }

    @Test
    void notSaveCustomerWhenNameIsBlank() {
        Customer customer = new Customer("", "+1234");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsNull() {
        Customer customer = new Customer("Abel", null);
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsBlank() {
        Customer customer = new Customer("Abel", "");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsNotUnique() {
        String phoneNumber = "+1234";
        Customer customer = new Customer("Abel", phoneNumber);
        Customer customer2 = new Customer("Bob", phoneNumber);
        customerRepository.save(customer);
        assertThatThrownBy(() -> customerRepository.save(customer2))
                .hasMessageContaining("could not execute statement")
                .isInstanceOf(DataIntegrityViolationException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isPresent();
        UUID id2 = customer2.getId();
        assertThat(customerRepository.findById(id2)).isNotPresent();
    }
}