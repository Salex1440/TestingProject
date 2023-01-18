package com.example.TestingProject.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer")
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customers_phone_number_unique",
                        columnNames = { "phone_number" }
                )
        }
)
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "name",
            nullable = false
    )
    @NotBlank
    private String name;

    @Column(
            name = "phone_number",
            nullable = false
    )
    @NotBlank
    private String phoneNumber;

    public Customer() {
    }

    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
