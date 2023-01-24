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

    @Test
    void validatePhoneNumberWhenIncorrect() {
        String phoneNumber1 = "-79876543210";
        String phoneNumber2 = "+7987654321a";
        String phoneNumber3 = "+7987654321";

        boolean valid1 = phoneNumberValidator.test(phoneNumber1);
        boolean valid2 = phoneNumberValidator.test(phoneNumber2);
        boolean valid3 = phoneNumberValidator.test(phoneNumber3);

        assertThat(valid1).isFalse();
        assertThat(valid2).isFalse();
        assertThat(valid3).isFalse();
    }
}
