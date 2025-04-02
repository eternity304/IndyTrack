package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType;

    @Column(name = "credit_value")
    private Long creditValue;

    @ElementCollection(targetClass = AcademicFocus.class)
    @CollectionTable(name = "course_academic_focus", joinColumns = @JoinColumn(name = "course_code"))
    @Column(name = "academic_focus")
    @Enumerated(EnumType.STRING)
    @Nullable
    private Set<AcademicFocus> academicFocus = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @Transient
    public String getSkuleUrl() {
        return "https://courses.skule.ca/course/" + this.code;
    }

    @ManyToMany
    @JoinTable(
        name = "course_prerequisites",
        joinColumns = @JoinColumn(name = "course_code"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_code")
    )
    private Set<Course> prerequisites = new HashSet<>();

    @ManyToMany(mappedBy = "prerequisites")      
    @JsonIgnore
    private Set<Course> isPrerequisiteFor = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<MinorRequirement> minorRequirements = new HashSet<>();

    @Transient
    public Set<String> getMinorNames() {
        return minorRequirements.stream()
            .map(req -> req.getMinor().getName())
            .collect(Collectors.toSet());
    }

    public Course(
        String code, 
        String name, 
        String description, 
        CourseType courseType, 
        Long creditValue, 
        Set<AcademicFocus> academicFocus,
        Set<Course> prerequisites
    ) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.courseType = courseType;
        this.creditValue = creditValue;
        this.academicFocus = academicFocus != null ? academicFocus : new HashSet<>();
        this.prerequisites = prerequisites;
    }
}
