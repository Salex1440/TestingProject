package com.example.TestingProject.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    @Query("SELECT id, name, phone_number " +
            "FROM customer WHERE phone_number = :phone_number")
    Optional<Customer> selectByPhoneNumber(
            @Param("phone_number") String phoneNumber);

}
