package com.example.TestingProject.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void notSaveCustomerWhenNameIsNull() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "+1234");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);

    }

    @Test
    void notSaveCustomerWhenNameIsBlank() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "", "+1234");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);

    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsNull() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", null);
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);

    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsBlank() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("Could not commit JPA transaction")
                .isInstanceOf(TransactionSystemException.class);

    }
}