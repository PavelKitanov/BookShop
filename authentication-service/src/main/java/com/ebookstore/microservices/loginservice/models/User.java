package com.ebookstore.microservices.loginservice.models;

import jakarta.persistence.*;
import jdk.jfr.BooleanFlag;
import lombok.*;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @BooleanFlag
    private Boolean enabled;

    @NonNull
    private String verificationCode;

    private String facebookId;

    private String googleId;

    public User(@NonNull String username,@NonNull String email,@NonNull String password,@NonNull Role role, @NonNull String verificationCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = false;
        this.verificationCode = verificationCode;
    }

    public User(@NonNull String username,@NonNull String email,@NonNull String password,@NonNull Role role, @NonNull String verificationCode, String facebookId, String googleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = true;
        this.verificationCode = verificationCode;
        this.facebookId = facebookId;
        this.googleId = googleId;
    }


    public User(@NonNull String username,@NonNull String email,@NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = false;
    }

    public User(@NonNull String username,@NonNull String email,@NonNull String password, String facebookId, String googleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = true;
        this.facebookId = facebookId;
        this.googleId = googleId;
    }
}
