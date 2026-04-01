package com.br.sccon.api.controller;

import com.br.sccon.api.controller.dto.PersonRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Cenário E2E: Criar, buscar e atualizar parcialmente uma pessoa")
    void fullPersonLifecycleTest() throws Exception {

        PersonRequestDTO newPerson = new PersonRequestDTO(
                null, "Novo Usuario", LocalDate.of(1990, 5, 20), LocalDate.now()
        );

        // 1. POST - Criar Pessoa
        String response = mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Novo Usuario"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();


        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(get("/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Usuario"));

        String jsonPatch = "{\"nome\": \"Nome Atualizado\"}";
        mockMvc.perform(patch("/person/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPatch))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.dataDeNascimento").exists());

        mockMvc.perform(get("/person/{id}/salary", id)
                        .param("output", "full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(greaterThanOrEqualTo(1558.00)));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar ID que não existe")
    void shouldReturn404WhenNotFoundInDatabase() throws Exception {
        mockMvc.perform(get("/person/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}