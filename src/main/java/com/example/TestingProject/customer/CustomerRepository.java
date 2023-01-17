package com.example.TestingProject.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    @Query("SELECT c " +
            "FROM Customer c " +
            "WHERE c.phoneNumber = :phone_number")
    Optional<Customer> selectByPhoneNumber(
            @Param("phone_number") String phoneNumber);

}
