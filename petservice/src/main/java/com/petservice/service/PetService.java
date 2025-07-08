package com.petservice.service;

import com.petservice.dto.PetDTO;
import java.util.List;

public interface PetService {

    PetDTO createPet(PetDTO petDTO);     
    PetDTO getPetById(Long id);        
    List<PetDTO> getAllPets();          
    PetDTO updatePet(Long id, PetDTO dto);  
    void deletePet(Long id);                 
}
