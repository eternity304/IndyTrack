package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@DiscriminatorValue("student")
@NoArgsConstructor
@Getter
@Setter
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Nullable
    private List<CoursePlan> coursePlans;
    
}
