package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import java.util.HashSet;
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

    public Course(
        String code, 
        String name, 
        String description, 
        CourseType courseType, 
        Long creditValue, 
        Set<AcademicFocus> academicFocus
    ) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.courseType = courseType;
        this.creditValue = creditValue;
        this.academicFocus = academicFocus != null ? academicFocus : new HashSet<>();
    }

}
