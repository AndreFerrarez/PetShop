package com.infnet.petShop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.petShop.model.Product;
import com.infnet.petShop.repository.ProductRepository;
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("Dog Food");
        product.setPrice(29.99);
        product.setCategory(1);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dog Food"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setName("Dog Food");
        repository.save(product);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dog Food"));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setName("Dog Food");
        Product saved = repository.save(product);

        mockMvc.perform(get("/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dog Food"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Dog Food");
        Product saved = repository.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Cat Food");
        updatedProduct.setPrice(39.99);
        updatedProduct.setCategory(2);

        mockMvc.perform(put("/products/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cat Food"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("Dog Food");
        Product saved = repository.save(product);

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetProductNotFound() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

}