package com.inde.indytrack.dto;

import java.util.Set;

import com.inde.indytrack.model.AcademicFocus;
import com.inde.indytrack.model.CourseType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {
    private String code;
    private String name;
    private String description;
    private Set<String> prerequisiteCourseCodes;
    private Set<String> minorNames;
    private CourseType courseType;
    private Long creditValue;
    private Set<AcademicFocus> academicFocus;
}
