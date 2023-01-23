package com.example.TestingProject.payment.stripe;

import com.example.TestingProject.payment.CardPaymentCharge;
import com.example.TestingProject.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

class StripeServiceTest {

    @Mock
    private StripeApi stripeApiMock;

    private StripeService stripeService;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramsArgumentCaptor;

    @Captor
    private ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        stripeService = new StripeService(stripeApiMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void chargeCardSuccessfully() throws StripeException {
        String cardSource = "source";
        String description = "description";
        BigDecimal amount = new BigDecimal("10.00");
        Currency currency = Currency.USD;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);
        Charge charge = new Charge();
        charge.setPaid(true);
        given(stripeApiMock.create(eq(params), any(RequestOptions.class))).willReturn(charge);

        CardPaymentCharge cardPaymentCharge = stripeService.chargeCard(cardSource, amount, currency, description);

        then(stripeApiMock).should().create(paramsArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());

        Map<String, Object> paramsArgumentCaptorValue = paramsArgumentCaptor.getValue();
        assertThat(paramsArgumentCaptorValue.size() == params.size());
        assertThat(paramsArgumentCaptorValue.get("amount")).isEqualTo(amount);
        assertThat(paramsArgumentCaptorValue.get("currency")).isEqualTo(currency);
        assertThat(paramsArgumentCaptorValue.get("source")).isEqualTo(cardSource);
        assertThat(paramsArgumentCaptorValue.get("description")).isEqualTo(description);

        RequestOptions options = requestOptionsArgumentCaptor.getValue();
        assertThat(options).isNotNull();

        assertThat(cardPaymentCharge.isCardDebited()).isTrue();
    }

    @Test
    void chargeCardThrowsWhenStripeThrows() throws StripeException {
        String cardSource = "source";
        String description = "description";
        BigDecimal amount = new BigDecimal("10.00");
        Currency currency = Currency.USD;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);

//        when(stripeApiMock.create(eq(params), any(RequestOptions.class))).thenThrow(StripeException.class);
        given(stripeApiMock.create(any(), any(RequestOptions.class))).willThrow(RuntimeException.class);

        assertThatThrownBy(() -> stripeService.chargeCard(cardSource, amount, currency, description))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot make stripe charge");

        then(stripeApiMock).should().create(paramsArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());

        Map<String, Object> paramsArgumentCaptorValue = paramsArgumentCaptor.getValue();
        assertThat(paramsArgumentCaptorValue.size() == params.size());
        assertThat(paramsArgumentCaptorValue.get("amount")).isEqualTo(amount);
        assertThat(paramsArgumentCaptorValue.get("currency")).isEqualTo(currency);
        assertThat(paramsArgumentCaptorValue.get("source")).isEqualTo(cardSource);
        assertThat(paramsArgumentCaptorValue.get("description")).isEqualTo(description);

        RequestOptions options = requestOptionsArgumentCaptor.getValue();
        assertThat(options).isNotNull();

    }


}