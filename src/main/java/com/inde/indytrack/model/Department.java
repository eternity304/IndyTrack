package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


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
