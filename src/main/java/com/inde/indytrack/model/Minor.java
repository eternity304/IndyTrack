package com.inde.indytrack.model;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "minors")
@Getter
@Setter
@NoArgsConstructor
public class Minor {
    @Id
    @NotEmpty
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "minor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MinorRequirement> requirements = new HashSet<>();
}
