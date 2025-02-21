package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "departments")
public class Department {

    @Id
    @NotEmpty
    private String code;

    @NotEmpty
    private String name;

    @NotEmpty
    private String contactEmail;

    public Department(String code, String name, String contactEmail){
        this.code = code;
        this.name = name;
        this.contactEmail = contactEmail;
    }

}
