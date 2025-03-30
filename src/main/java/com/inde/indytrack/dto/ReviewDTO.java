package com.inde.indytrack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private String courseCode;
    private Long studentId;
    private Integer rating;
    private String comment;
}
