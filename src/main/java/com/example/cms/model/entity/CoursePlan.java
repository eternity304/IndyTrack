package com.example.cms.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    @Column(name = "student_id")
    private Long studentId;

    @NotEmpty
    private String semester;

    @ManyToMany
    @JoinTable(
            name = "course_plan_courses",
            joinColumns = @JoinColumn(name = "course_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "course_code")
    )

    private Set<Course> courses;

    public CoursePlan(Long studentId, String semester, Set<Course> courses){
        this.studentId = studentId;
        this.semester = semester;
        this.courses = courses;
    }
}
