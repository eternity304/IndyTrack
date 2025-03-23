package com.inde.indytrack.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "minors")
@NoArgsConstructor
@Getter
@Setter
public class Minor {
    @Id
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "minor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MinorRequirement> requirements = new HashSet<>();

}
