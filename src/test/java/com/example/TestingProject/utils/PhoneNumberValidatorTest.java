package com.example.TestingProject.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({"+79876543210, true"})
    void validatePhoneNumberSuccessfully(String phoneNumber, String expected) {
        boolean valid = phoneNumberValidator.test(phoneNumber);

        assertThat(valid).isEqualTo(Boolean.valueOf(expected));
    }

    @Test
    @DisplayName("Should fail validation, because phone numbers are incorrect")
    void notValidatePhoneNumberWhenIncorrect() {
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
