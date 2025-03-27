package com.inde.indytrack.model;

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
    @Column(name = "student_id")
    Long studentId;

    @Column(name = "course_code")
    String courseCode;

    @Column(name = "comment_number")
    Long commentNumber;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(studentId.hashCode())
                + String.valueOf(courseCode.hashCode())
                + String.valueOf(commentNumber.hashCode());
        return concatString.hashCode();
    }

    public CommentKey(Long studentId, String courseCode, Long commentNumber) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.commentNumber = commentNumber;
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
        return studentId.equals(other.studentId) 
            && courseCode.equals(other.courseCode) 
            && commentNumber.equals(other.commentNumber);
    }
}
