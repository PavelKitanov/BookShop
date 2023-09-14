package com.ebookstore.microservices.loginservice.repositories;

import com.ebookstore.microservices.loginservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findUserByFacebookId(String facebookId);
    User findUserByVerificationCode(String verificationCode);


}
