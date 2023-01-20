package com.example.TestingProject.payment;

import com.example.TestingProject.customer.Customer;
import com.example.TestingProject.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void insertPayment() {
        Customer customer = new Customer("Alex", "+1234");
        customerRepository.save(customer);
        Payment payment = new Payment(customer, new BigDecimal("10.00"), Currency.USD, "card123", "description");

        paymentRepository.save(payment);

        Optional<Payment> paymentOptional = paymentRepository.findById(payment.getId());
        assertThat(paymentOptional)
                .isPresent()
                .hasValueSatisfying(p -> {
                    assertThat(p).isEqualTo(payment);
//                    assertThat(p).usingRecursiveComparison().isEqualTo(payment);
                });
    }

}