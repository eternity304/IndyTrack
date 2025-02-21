package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "course_plans")
public class CoursePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ElementCollection
    @CollectionTable(name = "course_plan_semesters", joinColumns = @JoinColumn(name = "course_plan_id"))
    @MapKeyColumn(name = "course_code")
    @Column(name = "semester")
    private Map<String, String> courseSemesterMap = new HashMap<>();

    public CoursePlan(Student student, Map<String, String> courseSemesterMap){
        this.student = student;
        this.courseSemesterMap = courseSemesterMap != null ? courseSemesterMap : new HashMap<>();
    }

    public void updateCourseSemester(String courseCode, String semester) {
        this.courseSemesterMap.put(courseCode, semester);
    }

}
