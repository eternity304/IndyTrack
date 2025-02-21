package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CommentKey implements Serializable {
    @Column(name = "studentId")
    Long studentId;

    @Column(name = "courseId")
    String courseId;

    @Column(name = "time")
    String time;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(studentId.hashCode())
                + String.valueOf(courseId.hashCode())
                + String.valueOf(time.hashCode());
        return concatString.hashCode();
    }

    public CommentKey(Long studentId, String courseId, String time) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (o == this)
            return true;
        if (!(o instanceof CommentKey))
            return false;
        CommentKey other = (CommentKey) o;
        return studentId.equals(other.studentId) && courseId.equals(other.courseId) && time.equals(other.time);
    }
}
