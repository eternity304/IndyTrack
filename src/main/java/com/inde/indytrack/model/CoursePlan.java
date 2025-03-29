package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.inde.indytrack.exception.SemesterFullException;

import java.util.ArrayList;
import java.util.List;
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
    
    @OneToMany(mappedBy = "coursePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SemesterCourses> semesterCoursesList = new ArrayList<>();

    public CoursePlan(Student student, Map<String, List<String>> semesterCourses) {
        this.student = student;

        if (semesterCourses != null) {
            for (Map.Entry<String, List<String>> entry : semesterCourses.entrySet()) {
                SemesterCourses semesterCoursesEntity = new SemesterCourses(entry.getKey(), entry.getValue(), this);
                semesterCoursesList.add(semesterCoursesEntity);
            }
        }
    }
}
