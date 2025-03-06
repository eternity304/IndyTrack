package com.inde.indytrack.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CoursePlanDTO {
    private Long studentId;
    private Map<String, List<String>> semesterCourses; // Map<"Fall 2024", ["GOT123", "MATH101"]>
}
