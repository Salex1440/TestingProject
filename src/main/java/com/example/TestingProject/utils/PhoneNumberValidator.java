package com.example.TestingProject.utils;

import java.util.function.Predicate;

public class PhoneNumberValidator implements Predicate<String> {

    private final String regex = "^\\+7\\d{10}$";

    @Override
    public boolean test(String phoneNumber) {
        return phoneNumber.matches(regex);
    }

}
