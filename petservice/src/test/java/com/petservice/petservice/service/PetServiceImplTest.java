package com.petservice.service;

import com.petservice.dto.PetDTO;
import com.petservice.model.Pet;
import com.petservice.repository.PetRepository;
import com.petservice.service.impl.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreatePetSuccessfully() {
        PetDTO dto = new PetDTO(null, "Loki", "Cat", 2, 1L);
        Pet saved = Pet.builder().id(10L).name("Loki").species("Cat").age(2).userId(1L).build();

        when(petRepository.save(any(Pet.class))).thenReturn(saved);

        PetDTO result = petService.createPet(dto);

        assertEquals("Loki", result.getName());
        assertEquals(1L, result.getUserId());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void shouldReturnPetByIdIfUserOwnsIt() {
        Pet pet = Pet.builder().id(1L).name("Loki").species("Cat").age(2).userId(42L).build();

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        PetDTO result = petService.getPetById(1L, 42L);

        assertEquals("Loki", result.getName());
        assertEquals(2, result.getAge());
    }

    @Test
    void shouldThrowExceptionIfPetNotFound() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                petService.getPetById(99L, 42L)
        );

        assertTrue(exception.getMessage().contains("Pet not found"));
    }

    @Test
    void shouldThrowExceptionIfUserAccessesOtherUsersPet() {
        Pet pet = Pet.builder().id(1L).userId(100L).build();
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                petService.getPetById(1L, 42L)
        );

        assertTrue(exception.getMessage().contains("Access denied"));
    }

    @Test
    void shouldUpdatePetSuccessfully() {
        Pet existing = Pet.builder().id(1L).name("Old").species("Dog").age(3).userId(42L).build();
        Pet updated = Pet.builder().id(1L).name("NewName").species("Cat").age(4).userId(42L).build();

        PetDTO dto = new PetDTO(null, "NewName", "Cat", 4, 42L);

        when(petRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(petRepository.save(any(Pet.class))).thenReturn(updated);

        PetDTO result = petService.updatePet(1L, dto);

        assertEquals("NewName", result.getName());
        assertEquals(4, result.getAge());
    }

    @Test
    void shouldDeletePetIfUserOwnsIt() {
        Pet pet = Pet.builder().id(1L).userId(42L).build();
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        petService.deletePet(1L, 42L);

        verify(petRepository).deleteById(1L);
    }

    @Test
    void shouldReturnListOfPetsByUserId() {
        List<Pet> pets = List.of(
                Pet.builder().id(1L).name("Pet1").userId(1L).build(),
                Pet.builder().id(2L).name("Pet2").userId(1L).build()
        );

        when(petRepository.findByUserId(1L)).thenReturn(pets);

        List<PetDTO> result = petService.getPetsByUserId("1");

        assertEquals(2, result.size());
    }
}
