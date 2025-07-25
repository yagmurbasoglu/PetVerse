package com.petservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petservice.dto.PetDTO;
import com.petservice.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@ActiveProfiles("test")
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePet() throws Exception {
        PetDTO dto = new PetDTO(null, "Loki", "Cat", 2, null);
        PetDTO created = new PetDTO(1L, "Loki", "Cat", 2, 42L);

        when(petService.createPet(any(PetDTO.class))).thenReturn(created);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("X-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Loki"))
                .andExpect(jsonPath("$.userId").value(42L));
    }

    @Test
    void shouldGetPetByIdIfUserOwnsIt() throws Exception {
        PetDTO dto = new PetDTO(1L, "Loki", "Cat", 2, 42L);

        when(petService.getPetById(1L, 42L)).thenReturn(dto);

        mockMvc.perform(get("/pets/1")
                        .header("X-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Loki"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldGetAllPetsOfUser() throws Exception {
        List<PetDTO> pets = List.of(
                new PetDTO(1L, "A", "Dog", 1, 42L),
                new PetDTO(2L, "B", "Cat", 2, 42L)
        );

        when(petService.getPetsByUserId("42")).thenReturn(pets);

        mockMvc.perform(get("/pets")
                        .header("X-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldUpdatePet() throws Exception {
        PetDTO request = new PetDTO(null, "Updated", "Cat", 3, null);
        PetDTO updated = new PetDTO(1L, "Updated", "Cat", 3, 42L);

        when(petService.updatePet(eq(1L), any(PetDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    void shouldDeletePet() throws Exception {
        doNothing().when(petService).deletePet(1L, 42L);

        mockMvc.perform(delete("/pets/1")
                        .header("X-User-Id", "42"))
                .andExpect(status().isNoContent());

        verify(petService).deletePet(1L, 42L);
    }
}
