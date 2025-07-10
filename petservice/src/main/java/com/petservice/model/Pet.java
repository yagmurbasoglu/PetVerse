package com.petservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private Integer age;

    public Pet() {}

    // Full constructor
    public Pet(Long id, String name, String species, Integer age) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
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
}