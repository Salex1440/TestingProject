package com.example.TestingProject.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping("/{customerId")
    public void makePayment(@PathVariable("customerId") UUID customerId,
                            @RequestBody PaymentRequest request) {
        paymentService.chargeCard(customerId, request);
    }

}
