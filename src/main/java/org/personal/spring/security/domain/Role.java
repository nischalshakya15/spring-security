package org.personal.spring.security.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @NotNull
    @Column(name = "name", length = 50)
    private String name;
}