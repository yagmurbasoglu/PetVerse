package com.petservice.service.impl;

import com.petservice.dto.PetDTO;
import com.petservice.model.Pet;
import com.petservice.repository.PetRepository;
import com.petservice.mapper.PetMapper;
import com.petservice.service.PetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    @Override
    public PetDTO createPet(PetDTO petDTO) {
        Pet entity = petMapper.toEntity(petDTO);
        Pet saved = petRepository.save(entity);
        return petMapper.toDto(saved);
    }

    @Override
    public PetDTO getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));
        return petMapper.toDto(pet);
    }

    @Override
    public List<PetDTO> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(petMapper::toDto)
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
        return petMapper.toDto(updated);
    }

    @Override
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Pet not found with ID: " + id);
        }
        petRepository.deleteById(id);
    }
}
