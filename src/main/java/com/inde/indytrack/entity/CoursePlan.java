package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "course_plans")
public class CoursePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @NotEmpty
    private String semester;

    @ManyToMany
    @JoinTable(
            name = "course_plan_courses",
            joinColumns = @JoinColumn(name = "course_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "course_code")
    )

    private Set<Course> courses;

    public CoursePlan(Student student, String semester, Set<Course> courses){
        this.student = student;
        this.semester = semester;
        this.courses = courses;
    }
}
