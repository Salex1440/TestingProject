package com.example.TestingProject.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void selectByPhoneNumber() {

    }

    @Test
    void saveCustomer() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "+1234");

        customerRepository.save(customer);

        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo(customer.getName());
//                    assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
                    assertThat(c).usingRecursiveComparison().isEqualTo(customer);
                });
    }
}