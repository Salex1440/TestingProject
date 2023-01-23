package com.example.TestingProject.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;


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
        String phoneNumber = "+1234";
        Customer customer = new Customer("Alex", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        when(customerRepositoryMock.selectByPhoneNumber(phoneNumber))
                .thenReturn(Optional.empty());

        CustomerRegistrationResponse response = customerRegistrationService.registerNewCustomer(request);
        then(customerRepositoryMock).should().save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer).usingRecursiveComparison().isEqualTo(customer);
        assertThat(request).usingRecursiveComparison().isEqualTo(new CustomerRegistrationResponse(customer));
    }

    @Test
    void notSaveCustomerWhenCustomerExists() {
        String phoneNumber = "+1234";
        Customer customer = new Customer("Alex", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        when(customerRepositoryMock.selectByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(customer));

        customerRegistrationService.registerNewCustomer(request);
        then(customerRepositoryMock).should().selectByPhoneNumber(phoneNumber);
        then(customerRepositoryMock).should(never()).save(any(Customer.class));
    }

    @Test
    void throwExceptionWhenPhoneNumberIsInUse() {
        String phoneNumber = "+1234";
        Customer customer = new Customer("Alex", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        when(customerRepositoryMock.selectByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(new Customer("Bob", phoneNumber)));

        assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(request))
                .hasMessageContaining(String.format("The phone number %s is already exists!", phoneNumber))
                .isInstanceOf(IllegalStateException.class);
        then(customerRepositoryMock).should().selectByPhoneNumber(phoneNumber);
        then(customerRepositoryMock).should(never()).save(any(Customer.class));
    }
}