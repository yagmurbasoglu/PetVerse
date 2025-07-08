package com.petservice.mapper;

import com.petservice.dto.PetDTO;
import com.petservice.model.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {

    Pet toEntity(PetDTO dto);

    PetDTO toDto(Pet entity);
}
