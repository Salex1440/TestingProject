package com.example.TestingProject.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @Test
    void validatePhoneNumberSuccessfully() {
        String phoneNumber = "+79876543210";

        boolean valid = phoneNumberValidator.test(phoneNumber);

        assertThat(valid).isTrue();
    }
}
