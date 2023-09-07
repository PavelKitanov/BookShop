package com.ebookstore.microservices.loginservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.beans.ConstructorProperties;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NonNull
    private String username;

    @NonNull
    @Column(nullable=false, unique=true)
    private String email;

    @NonNull
    @Column(nullable=false)
    private String password;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(@NonNull String username,@NonNull String email,@NonNull String password,@NonNull Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(@NonNull String username,@NonNull String email,@NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
