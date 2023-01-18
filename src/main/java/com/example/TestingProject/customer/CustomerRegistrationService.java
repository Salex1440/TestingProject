package com.example.TestingProject.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) {
        Customer customer = request.getCustomer();
        customerRepository
                .selectByPhoneNumber(customer.getPhoneNumber())
                .ifPresentOrElse(
                        c -> {
                            if (!c.getName().equals(customer.getName())) {
                                throw new IllegalStateException(
                                        String.format("The phone number %s is already exists!",
                                                customer.getPhoneNumber()));
                            }
                        },
                        () -> customerRepository.save(customer)
                );
    }
}
