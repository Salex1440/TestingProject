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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

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
        Payment payment = new Payment(customer,
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

}