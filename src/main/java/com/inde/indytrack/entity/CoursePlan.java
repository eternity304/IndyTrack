package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "coursePlans")
public class CoursePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
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
