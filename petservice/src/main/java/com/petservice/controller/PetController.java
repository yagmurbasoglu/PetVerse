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

    // ✅ Pet oluştur - token'daki kullanıcıya atanır
    @PostMapping
    public ResponseEntity<PetDTO> createPet(
            @RequestBody PetDTO petDTO,
            @RequestHeader("X-User-Id") String userId
    ) {
        petDTO.setUserId(Long.parseLong(userId));
        PetDTO created = petService.createPet(petDTO);
        return ResponseEntity.ok(created);
    }

    // ✅ Kullanıcı sadece kendi pet'ini görüntüleyebilir
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId
    ) {
        PetDTO pet = petService.getPetById(id, Long.parseLong(userId));
        return ResponseEntity.ok(pet);
    }

    // ✅ Kullanıcının tüm pet'lerini getir
    @GetMapping
    public ResponseEntity<List<PetDTO>> getUserPets(
            @RequestHeader("X-User-Id") String userId) {
        List<PetDTO> pets = petService.getPetsByUserId(userId);
        return ResponseEntity.ok(pets);
    }

    // ✅ Pet güncelleme — sadece sahibi güncelleyebilir
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long id,
            @RequestBody PetDTO dto,
            @RequestHeader("X-User-Id") String userId
    ) {
        dto.setUserId(Long.parseLong(userId)); // güvenlik için yeniden set
        PetDTO updated = petService.updatePet(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ✅ Pet silme — sadece sahibi silebilir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId
    ) {
        petService.deletePet(id, Long.parseLong(userId));
        return ResponseEntity.noContent().build();
    }
}
