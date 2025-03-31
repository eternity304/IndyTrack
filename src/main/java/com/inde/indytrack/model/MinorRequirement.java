package com.inde.indytrack.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import java.util.Set;
import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "minor_requirements")
@Getter
@Setter
@NoArgsConstructor
public class MinorRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "minor_name", referencedColumnName = "name")
    private Minor minor;

    @Column(name = "required_credits", nullable = false)
    private Double requiredCredits;

    @ManyToMany
    @JoinTable(
        name = "minor_requirement_courses", 
        joinColumns = @JoinColumn(name = "minor_requirement_id"), 
        inverseJoinColumns = @JoinColumn(name = "course_code")
    )
    private Set<Course> courses = new HashSet<>();
}
