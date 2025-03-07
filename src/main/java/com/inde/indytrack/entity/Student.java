package com.inde.indytrack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@Getter
@Setter
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private List<CoursePlan> coursePlans;
}
