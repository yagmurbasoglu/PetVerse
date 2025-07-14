package com.petverse.dto;

public class PetDTO {
    private Long id;
    private String name;
    private String species;
    private Integer age;
    private Long userId;

    // Getter ve Setter'lar
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
