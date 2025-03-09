package com.inde.indytrack.entity;

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
@Table(name = "semesters")
public class SemesterCourses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String semester;  

    private String courseCode; 

    @ElementCollection
    @CollectionTable(name = "semesterCoursesList", joinColumns = @JoinColumn(name = "semesterId"))
    @Column(name = "courseCode")
    private List<String> courses = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "coursePlanId", nullable=false)
    private CoursePlan coursePlan;

    public SemesterCourses(String semester, List<String> courses, CoursePlan coursePlan) {
        this.semester = semester;
        this.courses = courses != null ? courses : new ArrayList<>();
        this.coursePlan = coursePlan;
    }

    
}
