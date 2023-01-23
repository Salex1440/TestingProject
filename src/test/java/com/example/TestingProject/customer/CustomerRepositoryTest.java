package com.example.TestingProject.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
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
                    assertThat(c).usingRecursiveComparison().isEqualTo(customer);
                });
    }

    @Test
    void notSaveCustomerWhenNameIsNull() {
        Customer customer = new Customer(null, "+1234");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenNameIsBlank() {
        Customer customer = new Customer("", "+1234");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsNull() {
        Customer customer = new Customer("Abel", null);
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsBlank() {
        Customer customer = new Customer("Abel", "");
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isNotPresent();
    }

    @Test
    void notSaveCustomerWhenPhoneNumberIsNotUnique() {
        String phoneNumber = "+12345";
        Customer customer = new Customer("Abel", phoneNumber);
        Customer customer2 = new Customer("Bob", phoneNumber);
        customerRepository.save(customer);
        assertThatThrownBy(() -> customerRepository.save(customer2))
                .hasMessageContaining("not-null property references a null or transient value")
                .isInstanceOf(DataIntegrityViolationException.class);
        UUID id = customer.getId();
        assertThat(customerRepository.findById(id)).isPresent();
        UUID id2 = customer2.getId();
        assertThat(customerRepository.findById(id2)).isNotPresent();
    }
}