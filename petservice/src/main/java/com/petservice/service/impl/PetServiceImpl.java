package com.petservice.service.impl;

import com.petservice.dto.PetDTO;
import com.petservice.model.Pet;
import com.petservice.repository.PetRepository;
import com.petservice.service.PetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public PetDTO createPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setAge(petDTO.getAge());
        pet.setSpecies(petDTO.getSpecies());
        pet.setUserId(petDTO.getUserId()); // 🟢 Kullanıcı ID'si

        Pet saved = petRepository.save(pet);
        return convertToDTO(saved);
    }

    // ✅ Kullanıcı sadece kendi pet'ini getirebilir
    @Override
    public PetDTO getPetById(Long id, Long userId) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        if (!pet.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied to pet with ID: " + id);
        }

        return convertToDTO(pet);
    }

    @Override
    public List<PetDTO> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Pet güncelleme (kontrolü Controller'da yapılıyor)
    @Override
    public PetDTO updatePet(Long id, PetDTO dto) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        existingPet.setName(dto.getName());
        existingPet.setSpecies(dto.getSpecies());
        existingPet.setAge(dto.getAge());
        existingPet.setUserId(dto.getUserId()); // güvenlik için tekrar atanır

        Pet updated = petRepository.save(existingPet);
        return convertToDTO(updated);
    }

    // ✅ Silme işlemi sadece kendi pet'i için yapılabilir
    @Override
    public void deletePet(Long id, Long userId) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        if (!pet.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied to delete pet with ID: " + id);
        }

        petRepository.deleteById(id);
    }

    // ✅ Kullanıcının tüm pet'lerini getir
    @Override
    public List<PetDTO> getPetsByUserId(String userId) {
        Long id = Long.parseLong(userId);
        return petRepository.findByUserId(id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PetDTO convertToDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setAge(pet.getAge());
        dto.setUserId(pet.getUserId());
        return dto;
    }
}