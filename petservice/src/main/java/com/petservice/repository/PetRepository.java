package com.petservice.repository;

import com.petservice.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    // Kullanıcıya ait tüm pet'leri getir
    List<Pet> findByUserId(Long userId);

    // Belirli kullanıcıya ait belirli pet'i getir (isteğe bağlı, ekstra güvenlik için kullanılabilir)
    Pet findByIdAndUserId(Long id, Long userId);
}
    List<Pet> findByUserId(Long userId); // 🔥 bunu ekliyoruz
}
