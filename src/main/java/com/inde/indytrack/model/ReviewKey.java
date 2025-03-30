package com.inde.indytrack.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewKey implements Serializable {
    private String courseCode;
    private Long studentId;

    @Override
    public int hashCode() {
        String concatString = String.valueOf(courseCode.hashCode()) + String.valueOf(studentId.hashCode());
        return concatString.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewKey reviewKey = (ReviewKey) o;
        return courseCode.equals(reviewKey.courseCode) && studentId.equals(reviewKey.studentId);
    }
}
