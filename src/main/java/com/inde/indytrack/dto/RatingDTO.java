package com.inde.indytrack.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Getter
@Setter
public class RatingDTO {
    private String courseCode;
    private Long studentId;
    
    @Min(1)
    @Max(5)
    private Integer ratingValue;
}
