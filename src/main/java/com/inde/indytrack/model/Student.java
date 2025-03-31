package com.inde.indytrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@DiscriminatorValue("student")
@NoArgsConstructor
@AllArgsConstructor
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
