package com.example.TestingProject.payment;

public enum Currency {
    USD("USD"),
    RUB("RUB");

    private final String name;

    Currency(String name) {
        this.name = name;
    }
}
