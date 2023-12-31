package com.ebookstore.microservices.loginservice.services.impl;

import com.ebookstore.microservices.loginservice.client.GoogleClient;
import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.exceptions.GoogleUserLoginException;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.models.google.GoogleUser;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import com.ebookstore.microservices.loginservice.security.jwt.JwtUtils;
import com.ebookstore.microservices.loginservice.security.services.UserDetailsImpl;
import com.ebookstore.microservices.loginservice.services.GoogleService;
import com.ebookstore.microservices.loginservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class GoogleServiceImpl implements GoogleService {


    @Autowired
    private GoogleClient googleClient;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    Random random = new Random();

    @Override
    public JwtResponse loginUser(String googleAccessToken) {
        GoogleUser googleUser = googleClient.getUser(googleAccessToken);

        String jwtToken = userService.findUserByGoogleId(googleUser.getId())
                .or(() -> Optional.ofNullable(userService.registerUser(convertTo(googleUser), String.valueOf(Roles.ROLE_USER))))
                .map(UserDetailsImpl::build)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(jwtUtils::generateJwtToken)
                .orElseThrow(() ->
                        new GoogleUserLoginException("unable to login google user id " + googleUser.getId()));
        User user = userService.findUserByGoogleId(googleUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with this username does not exist!"));

        return new JwtResponse(jwtToken, user.getUsername(), user.getEmail(), user.getRole().toString(), user.getUserId());
    }

    @Override
    public User convertTo(GoogleUser googleUser) {
        return new User(generateUsername(googleUser.getName()), googleUser.getEmail(), generatePassword(8), (String) null, googleUser.getId());
    }

    @Override
    public String generateUsername(String username) {
        int number = random.nextInt(999999);

        return String.format("%s.%06d", username, number);
    }

    @Override
    public String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }
}
