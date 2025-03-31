package com.inde.indytrack.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinorRequirementDTO {
    private Long id;
    private Double requiredCredits;
    private Set<String> courseCodes;
}