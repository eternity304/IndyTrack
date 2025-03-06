package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "courses")
public class Course {

    @Id
    @NotEmpty
    private String code;

    @NotEmpty
    private String name;

    @NotEmpty
    @Column(name = "description", length = 2000)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "coursePrerequisites",
        joinColumns = @JoinColumn(name = "courseId"),
        inverseJoinColumns = @JoinColumn(name = "prerequisitesId")
    )
    private Set<Course> prerequisites = new HashSet<>();  
  

    public Course(
            String code,
            String name,
            Optional<String> description,
            Set<Course> prerequisites)
    {
        this.code = code;
        this.name = name;
        this.description = description.orElse(null);
        this.prerequisites = prerequisites != null ? prerequisites : new HashSet<>();
    }

}
