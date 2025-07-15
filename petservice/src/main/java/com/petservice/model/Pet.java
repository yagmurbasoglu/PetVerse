package com.petservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private Integer age;

    private Long userId;
}
    private Long userId; // 🔥 Eklenen alan

    public Pet() {}

    public Pet(Long id, String name, String species, Integer age, Long userId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.userId = userId;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
