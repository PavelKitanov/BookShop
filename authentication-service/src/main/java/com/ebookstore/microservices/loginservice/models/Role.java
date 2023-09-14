package com.ebookstore.microservices.loginservice.models;

import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private Roles name;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<User> users;

    public Role(Roles name){
        this.name = name;
        this.users = new ArrayList<>();
    }

}
