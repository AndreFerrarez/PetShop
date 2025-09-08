package com.infnet.petShop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.petShop.model.Supplier;
import com.infnet.petShop.repository.SupplierRepository;
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
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    public void testCreateSupplier() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("Pet Supplies Inc");
        supplier.setContact("contact@psi.com");
        supplier.setProductType("Food");

        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pet Supplies Inc"));
    }

    @Test
    public void testGetAllSuppliers() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("Pet Supplies Inc");
        repository.save(supplier);

        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pet Supplies Inc"));
    }

    @Test
    public void testGetSupplierById() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("Pet Supplies Inc");
        Supplier saved = repository.save(supplier);

        mockMvc.perform(get("/suppliers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pet Supplies Inc"));
    }



    @Test
    public void testDeleteSupplier() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("Pet Supplies Inc");
        Supplier saved = repository.save(supplier);

        mockMvc.perform(delete("/suppliers/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetSupplierNotFound() throws Exception {
        mockMvc.perform(get("/suppliers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateSupplier() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName("Pet Supplies Inc");
        Supplier saved = repository.save(supplier);

        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setName("Global Pets");
        updatedSupplier.setContact("global@pet.com");
        updatedSupplier.setProductType("Toys");

        mockMvc.perform(put("/suppliers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSupplier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Global Pets"));
    }

}