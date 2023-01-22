package com.example.TestingProject.payment;

import com.example.TestingProject.customer.Customer;
import com.example.TestingProject.customer.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Mock
    private CardPaymentCharger cardPaymentChargerMock;

    @InjectMocks
    private PaymentService paymentService;

    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;


    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void chargeCardSuccess() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer("Alex", "+1234");
        customer.setId(id);
        when(customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));
        Payment payment = new Payment(null,
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "description");
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(true);
        when(cardPaymentChargerMock.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()))
                .thenReturn(cardPaymentCharge);

        paymentService.chargeCard(id, paymentRequest);

        then(paymentRepositoryMock).should().save(paymentArgumentCaptor.capture());
        Payment paymentCaptured = paymentArgumentCaptor.getValue();
        payment.setCustomer(customer);
        assertThat(paymentCaptured).isEqualTo(payment);
    }

    @Test
    void chargeCardThrowsExceptionWhenCustomerNotFound() {
        UUID id = UUID.randomUUID();
        when(customerRepositoryMock.findById(id)).thenReturn(Optional.empty());
        Payment payment = new Payment(null,
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "description");
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        assertThatThrownBy(() -> paymentService.chargeCard(id, paymentRequest))
                .hasMessageContaining(String.format("Customer with id [%s] not found!", id))
                .isInstanceOf(IllegalStateException.class);
        then(cardPaymentChargerMock).shouldHaveNoInteractions();
        then(paymentRepositoryMock).should(never()).save(any(Payment.class));
    }

    @Test
    void chargeCardThrowsExceptionWhenCurrencyIsNotSupported() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer("Alex", "+1234");
        customer.setId(id);
        when(customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));
        Payment payment = new Payment(null,
                new BigDecimal("10.00"),
                null,
                "card123",
                "description");
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        assertThatThrownBy(() -> paymentService.chargeCard(id, paymentRequest))
                .hasMessageContaining("Currency is not supported!")
                .isInstanceOf(IllegalStateException.class);
        then(cardPaymentChargerMock).shouldHaveNoInteractions();
        then(paymentRepositoryMock).should(never()).save(any(Payment.class));
    }

    @Test
    void chargeCardThrowsExceptionWhenCardNotDebited() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer("Alex", "+1234");
        customer.setId(id);
        when(customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));
        Payment payment = new Payment(null,
                new BigDecimal("10.00"),
                Currency.USD,
                "card123",
                "description");
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        CardPaymentCharge cardPaymentCharge = new CardPaymentCharge(false);
        when(cardPaymentChargerMock.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()))
                .thenReturn(cardPaymentCharge);

        assertThatThrownBy(() -> paymentService.chargeCard(id, paymentRequest))
                .hasMessageContaining(String.format("Card [%s] is not debited for customer with id [%s]!",
                        paymentRequest.getPayment().getSource(),
                        id))
                .isInstanceOf(IllegalStateException.class);
        then(paymentRepositoryMock).should(never()).save(any(Payment.class));
    }

}