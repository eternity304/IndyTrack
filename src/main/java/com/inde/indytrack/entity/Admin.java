package com.inde.indytrack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@NoArgsConstructor
@Getter
@Setter
@Data
public class Admin extends User {

}
