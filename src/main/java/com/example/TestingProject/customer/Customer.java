package com.example.TestingProject.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer")
@Table(name = "customers")
public class Customer {

    @Id
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    public Customer() {
    }

    public Customer(UUID id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
