package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "semester_courses")
public class SemesterCourses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String semester;  

    @Column(name = "course_code")
    private String courseCode; 

    @ElementCollection
    @CollectionTable(name = "semester_courses_list", joinColumns = @JoinColumn(name = "semester_id"))
    @Column(name = "course_code")
    private List<String> courses = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_plan_id", nullable=false)
    private CoursePlan coursePlan;

    public SemesterCourses(String semester, List<String> courses, CoursePlan coursePlan) {
        this.semester = semester;
        this.courses = courses != null ? courses : new ArrayList<>();
        this.coursePlan = coursePlan;
    }

    
}
