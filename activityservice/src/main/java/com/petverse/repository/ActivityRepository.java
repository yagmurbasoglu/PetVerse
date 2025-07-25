package com.petverse.repository;

import com.petverse.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByPetId(Long petId);
    List<Activity> findAllByPetIdAndCreatedAtBetween(Long petId, LocalDateTime start, LocalDateTime end);

}