package com.petservice.service;

import com.petservice.dto.PetDTO;
import java.util.List;

public interface PetService {

    PetDTO createPet(PetDTO petDTO);

    // 🔐 Sadece userId'ye ait pet getir
    PetDTO getPetById(Long id, Long userId);

    // Tüm petler: artık kullanılmıyor (isteğe bağlı kaldırılabilir)
    List<PetDTO> getAllPets(); 

    // 🔐 Güncelleme - kontrol controller'da yapılabilir, istersen burada da userId eklenebilir
    PetDTO updatePet(Long id, PetDTO dto);

    // 🔐 Sadece userId'ye ait pet'i silebilir
    void deletePet(Long id, Long userId);

    // Kullanıcının tüm pet'lerini getir
    List<PetDTO> getPetsByUserId(String userId);
}
