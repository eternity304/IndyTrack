package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CommentKey implements Serializable {
    @Column(name = "studentId")
    Long studentId;

    @Column(name = "courseCode")
    String courseCode;

    @Column(name = "time")
    String time;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(studentId.hashCode())
                + String.valueOf(courseCode.hashCode())
                + String.valueOf(time.hashCode());
        return concatString.hashCode();
    }

    public CommentKey(Long studentId, String courseCode, String time) {
        this.studentId = studentId;
        this.courseCode = courseCode;
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
        return studentId.equals(other.studentId) && courseCode.equals(other.courseCode) && time.equals(other.time);
    }
}
