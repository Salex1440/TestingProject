package com.example.TestingProject.payment;

import com.example.TestingProject.customer.Customer;
import com.example.TestingProject.customer.CustomerRegistrationRequest;
import com.example.TestingProject.customer.CustomerRegistrationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPaymentSuccessfully() throws Exception {
        Customer customer = new Customer("Alex", "+1234");
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        ResultActions customerRegResultActions = mockMvc.perform(put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(request))
        );

        MvcResult mvcResult = customerRegResultActions.andReturn();
        String customerRegResponseString = mvcResult.getResponse().getContentAsString();
        CustomerRegistrationResponse customerRegistrationResponse = stringToCustomerRegResponse(customerRegResponseString);

        assertNotNull(customerRegistrationResponse);
        customerRegResultActions
                .andExpect(status().isOk());

        UUID customerId = customerRegistrationResponse.getCustomer().getId();
    }


    private String objectToJson(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }

    private CustomerRegistrationResponse stringToCustomerRegResponse(String str) {
        try {
            return new ObjectMapper().readValue(str, CustomerRegistrationResponse.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
