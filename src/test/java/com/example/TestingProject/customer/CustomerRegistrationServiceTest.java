package com.example.TestingProject.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService customerRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRegistrationService = new CustomerRegistrationService(customerRepositoryMock);
    }

    @Test
    void saveNewCustomer() {
        UUID id = UUID.randomUUID();
        String phoneNumber = "+1234";
        Customer customer = new Customer(id, "Alex", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        when(customerRepositoryMock.selectByPhoneNumber(phoneNumber))
                .thenReturn(Optional.empty());

        customerRegistrationService.registerNewCustomer(request);
        then(customerRepositoryMock).should().save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer).usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    void notSaveCustomerWhenCustomerExists() {
        UUID id = UUID.randomUUID();
        String phoneNumber = "+1234";
        Customer customer = new Customer(id, "Alex", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        when(customerRepositoryMock.selectByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(customer));

        customerRegistrationService.registerNewCustomer(request);
        then(customerRepositoryMock).should().selectByPhoneNumber(phoneNumber);
        then(customerRepositoryMock).should(never()).save(any(Customer.class));
    }

    @Test
    void throwExceptionWhenPhoneNumberIsInUse() {
        UUID id = UUID.randomUUID();
        String phoneNumber = "+1234";
        Customer customer = new Customer(id, "Alex", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        when(customerRepositoryMock.selectByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(new Customer(UUID.randomUUID(), "Bob", phoneNumber)));

        assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(request))
                .hasMessageContaining(String.format("The phone number %s is already exists!", phoneNumber))
                .isInstanceOf(IllegalStateException.class);
        then(customerRepositoryMock).should().selectByPhoneNumber(phoneNumber);
        then(customerRepositoryMock).should(never()).save(any(Customer.class));
    }
}