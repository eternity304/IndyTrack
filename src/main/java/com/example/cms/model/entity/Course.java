package com.example.cms.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.swing.text.html.Option;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

//    @Nullable
//    private List<String> comments = new ArrayList<>();

//    @Nullable
//    private Course prequisite;

    public Course(
            String code,
            String name,
            Optional<String> description)
    {
        this.code = code;
        this.name = name;

        this.description = description.orElse(null);
    }

}
