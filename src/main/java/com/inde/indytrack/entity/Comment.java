package com.inde.indytrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    @EmbeddedId
    CommentKey commentId;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "studentId")
    @JsonIgnoreProperties({"comments"})
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "courseId")
    @JsonIgnoreProperties({"comments"})
    private Course course;

    @Column(name = "time", insertable = false, updatable = false)
    private String time;

    @NotEmpty
    @Column(name = "body", length=1000)
    private String body;


}
