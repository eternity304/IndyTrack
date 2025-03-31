package com.inde.indytrack.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> intendedMinorNames;
}
