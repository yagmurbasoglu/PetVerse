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


        Pet saved = petRepository.save(pet);
        return convertToDTO(saved);
       
    }

    @Override
    public PetDTO getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));
        return convertToDTO(pet);
    }

    @Override
    public List<PetDTO> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetDTO updatePet(Long id, PetDTO dto) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        existingPet.setName(dto.getName());
        existingPet.setSpecies(dto.getSpecies());
        existingPet.setAge(dto.getAge());

        Pet updated = petRepository.save(existingPet);
        return convertToDTO(updated);
    }

    @Override
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Pet not found with ID: " + id);
        }
        petRepository.deleteById(id);
    }

    private PetDTO convertToDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setAge(pet.getAge());
        return dto;
    }
}
