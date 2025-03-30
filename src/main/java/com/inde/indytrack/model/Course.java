package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inde.indytrack.exception.InvalidRatingException;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

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
        this.averageRating = 0.0;
        this.ratingCount = 0;
    }

    public void updateAverageRating(Integer newRating) {
        if (newRating < 1 || newRating > 5) {
            throw new InvalidRatingException();
        }
        if (this.averageRating == null) {
            this.averageRating = 0.0;
        }
        if (this.ratingCount == null) {
            this.ratingCount = 0;
        }
        double totalRating = this.averageRating * this.ratingCount + newRating;
        this.ratingCount++;
        this.averageRating = Math.round((totalRating / this.ratingCount) * 100.0) / 100.0;
    }

    public void removeRating(Integer existingRating) {
        if (this.ratingCount <= 0) {
            throw new IllegalStateException("No ratings to remove");
        }
        double totalRating = this.averageRating * this.ratingCount - existingRating;
        this.ratingCount--;
        if (this.ratingCount == 0) {
            this.averageRating = 0.0;
        } else {
            this.averageRating = Math.round((totalRating / this.ratingCount) * 100.0) / 100.0;
        }
    }
}
