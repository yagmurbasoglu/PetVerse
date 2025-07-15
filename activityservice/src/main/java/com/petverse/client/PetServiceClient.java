package com.petverse.client;

import com.petverse.dto.PetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.petverse.config.FeignInterceptorConfig;


@FeignClient(
    name = "petservice",
    configuration = FeignInterceptorConfig.class // ⭐ Buraya dikkat
)
public interface PetServiceClient {
    @GetMapping("/pets/{id}")
    PetDTO getPetById(@PathVariable("id") Long id);
}