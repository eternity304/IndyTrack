package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Getter
@Setter
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private ArrayList<CoursePlan> coursePlans = new ArrayList<>();

}
