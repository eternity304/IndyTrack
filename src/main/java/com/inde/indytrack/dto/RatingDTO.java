package com.inde.indytrack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {
    private String courseCode;
    private Long studentId;
    private Integer ratingValue;
}
