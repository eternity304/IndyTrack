package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
        name = "student_intended_minors", 
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "minor_name", referencedColumnName = "name")
    )
    private Set<Minor> intendedMinors = new HashSet<>();
    
}
