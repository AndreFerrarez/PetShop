package com.infnet.petShop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.petShop.model.Customer;
import com.infnet.petShop.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    public void testCreateCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setName("Alice Smith");
        customer.setEmail("alice@example.com");
        customer.setPhone("123456789");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        Customer customer = new Customer();
        customer.setName("Alice Smith");
        repository.save(customer);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice Smith"));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        Customer customer = new Customer();
        customer.setName("Alice Smith");
        Customer saved = repository.save(customer);

        mockMvc.perform(get("/customers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setName("Alice Smith");
        Customer saved = repository.save(customer);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Bob Smith");
        updatedCustomer.setEmail("bob@example.com");
        updatedCustomer.setPhone("987654321");

        mockMvc.perform(put("/customers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob Smith"));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setName("Alice Smith");
        Customer saved = repository.save(customer);

        mockMvc.perform(delete("/customers/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCustomerNotFound() throws Exception {
        mockMvc.perform(get("/customers/999"))
                .andExpect(status().isNotFound());
    }
}