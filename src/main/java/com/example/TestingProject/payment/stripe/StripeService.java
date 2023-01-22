package com.example.TestingProject.payment.stripe;

import com.example.TestingProject.payment.CardPaymentCharge;
import com.example.TestingProject.payment.CardPaymentCharger;
import com.example.TestingProject.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService implements CardPaymentCharger {

    private final static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
            .build();

    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);

        try {
            Charge charge = Charge.create(params, requestOptions);
            Boolean chargePaid = charge.getPaid();
            return new CardPaymentCharge(chargePaid);
        } catch (StripeException e) {
            throw new IllegalStateException("Cannot make stripe charge", e);
        }

    }

}
