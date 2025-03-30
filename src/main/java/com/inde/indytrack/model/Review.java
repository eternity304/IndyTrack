package com.inde.indytrack.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Getter
@Setter
public class Review {
    @EmbeddedId
    private ReviewKey id;

    @ManyToOne
    @MapsId("course_code")
    @JoinColumn(name = "course_code")
    @JsonIgnoreProperties({"reviews"})
    private Course course;

    @ManyToOne
    @MapsId("student_id")
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"reviews"})
    private Student student;
    
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    @Column(name = "upload_time", insertable = false, updatable = false)
    private String uploadTime;
}
