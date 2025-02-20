package com.example.cms.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @ElementCollection
    @CollectionTable(name = "course_plan_semesters", joinColumns = @JoinColumn(name = "course_plan_id"))
    @MapKeyColumn(name = "course_code")
    @Column(name = "semester")
    private Map<String, String> courseSemesterMap = new HashMap<>();

    public CoursePlan(Long studentId, Map<String, String> courseSemesterMap) {
        this.studentId = studentId;
        this.courseSemesterMap = courseSemesterMap != null ? courseSemesterMap : new HashMap<>();
    }

    public void updateCourseSemester(String courseCode, String semester) {
        this.courseSemesterMap.put(courseCode, semester);
    }
}
