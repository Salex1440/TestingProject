package com.example.TestingProject.payment;

import com.example.TestingProject.customer.Customer;
import com.example.TestingProject.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Customer with id [%s] not found!", customerId));
        }

        try {
            Currency currency = Currency.valueOf(paymentRequest.getPayment().getCurrency().name());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalStateException("Currency is not supported!");
        }

        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );

        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(
                    String.format("Card [%s] is not debited for customer with id [%s]!",
                            paymentRequest.getPayment().getSource(),
                            customerId));
        }

        paymentRequest.getPayment().setCustomer(customer.get());
        paymentRepository.save(paymentRequest.getPayment());

    }

}
