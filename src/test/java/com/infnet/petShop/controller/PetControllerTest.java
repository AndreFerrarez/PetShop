package com.infnet.petShop.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.petShop.model.Pet;
import com.infnet.petShop.repository.PetRepository;
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
class PetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    public void testCreatePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("Rex");
        pet.setSpecies("Dog");
        pet.setOwnerName("Alice");

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    @Test
    public void testGetAllPets() throws Exception {
        Pet pet = new Pet();
        pet.setName("Rex");
        repository.save(pet);

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rex"));
    }

    @Test
    public void testGetPetById() throws Exception {
        Pet pet = new Pet();
        pet.setName("Rex");
        Pet saved = repository.save(pet);

        mockMvc.perform(get("/pets/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    @Test
    public void testUpdatePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("Rex");
        Pet saved = repository.save(pet);

        Pet updatedPet = new Pet();
        updatedPet.setName("Max");
        updatedPet.setSpecies("Cat");
        updatedPet.setOwnerName("Bob");

        mockMvc.perform(put("/pets/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Max"));
    }

    @Test
    public void testDeletePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("Rex");
        Pet saved = repository.save(pet);

        mockMvc.perform(delete("/pets/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetPetNotFound() throws Exception {
        mockMvc.perform(get("/pets/999"))
                .andExpect(status().isNotFound());
    }

}