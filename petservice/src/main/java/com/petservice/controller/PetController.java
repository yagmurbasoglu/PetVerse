package com.petservice.controller;

import com.petservice.dto.PetDTO;
import com.petservice.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

@PostMapping
public ResponseEntity<PetDTO> createPet(
        @RequestBody PetDTO petDTO,
        @RequestHeader("X-User-Id") String userId
) {
    petDTO.setUserId(Long.parseLong(userId));
    PetDTO created = petService.createPet(petDTO);
    return ResponseEntity.ok(created);
}


    //Belirli ID'ye sahip pet getir
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        PetDTO pet = petService.getPetById(id);
        return ResponseEntity.ok(pet);
    }

@GetMapping
public ResponseEntity<List<PetDTO>> getUserPets(
        @RequestHeader("X-User-Id") String userId) {

    List<PetDTO> pets = petService.getPetsByUserId(userId);
    return ResponseEntity.ok(pets);
}


    // Pet güncelle
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Long id, @RequestBody PetDTO dto) {
        PetDTO updated = petService.updatePet(id, dto);
        return ResponseEntity.ok(updated);
    }

    //Pet sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}