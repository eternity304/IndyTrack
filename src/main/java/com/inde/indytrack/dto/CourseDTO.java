package com.inde.indytrack.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {
    private String code;
    private String name;
    private String description;
    private Set<String> prerequisitesCodes;
}
