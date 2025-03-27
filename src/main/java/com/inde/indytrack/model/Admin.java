package com.inde.indytrack.model;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("admin")
@NoArgsConstructor
@Getter
@Setter
public class Admin extends User {

}
