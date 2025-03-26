package com.inde.indytrack.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "minor_requirements")
@NoArgsConstructor
@Getter
@Setter
public class MinorRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "required_credits")
    private Long requiredCredits;

    @ManyToOne
    @JoinColumn(name = "minor_name", nullable = false)
    private Minor minor;

    @ManyToMany
    @JoinTable(
        name = "minor_requirement_courses",
        joinColumns = @JoinColumn(name = "minor_requirement_id"),
        inverseJoinColumns = @JoinColumn(name = "course_code")
    )
    private Set<Course> courses = new HashSet<>();
    
    
    
}
