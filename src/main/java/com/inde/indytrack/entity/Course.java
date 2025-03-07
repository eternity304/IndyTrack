package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "courses")
public class Course {

    @Id
    @NotEmpty
    private String code;

    @NotEmpty
    private String name;

    @NotEmpty
    @Column(name = "description", length = 500)
    private String description;

    public Course(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

}
