package com.example.TestingProject.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomerRegistrationResponse {

    private final Customer customer;

    public CustomerRegistrationResponse(
            @JsonProperty("customer")Customer customer) {
        this.customer = customer;
    }

}
