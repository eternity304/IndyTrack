package com.inde.indytrack.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinorDTO {
    private String name;
    private Set<MinorRequirementDTO> requirements;
}
