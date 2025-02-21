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
    private Long id;

    @NotEmpty
    @Column(name = "student_id")
    private Long studentId;

    @ElementCollection
    @CollectionTable(name = "course_plan_semesters", joinColumns = @JoinColumn(name = "course_plan_id"))
    @MapKeyColumn(name = "course_code")
    @Column(name = "semester")
    private Map<String, String> courseSemesterMap = new HashMap<>();

    public CoursePlan(Long studentId, String semester, Set<Course> courses){
        this.studentId = studentId;
        this.semester = semester;
        this.courses = courses;
    }
}
