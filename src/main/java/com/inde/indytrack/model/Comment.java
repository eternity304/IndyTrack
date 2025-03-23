package com.inde.indytrack.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    @EmbeddedId
    private CommentKey id;

    @ManyToOne
    @MapsId("student_id")
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"comments"})
    private Student student;

    @ManyToOne
    @MapsId("course_code")
    @JoinColumn(name = "course_code")
    @JsonIgnoreProperties({"comments"})
    private Course course;

    @Column(name = "comment_number", insertable = false, updatable = false)
    private Long commentNumber;

    @Column(name = "upload_time", insertable = false, updatable = false)
    private String uploadTime;

    @NotEmpty
    @Column(name = "body", length=1000)
    private String body;


}
