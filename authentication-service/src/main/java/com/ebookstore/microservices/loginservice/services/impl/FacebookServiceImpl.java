package com.ebookstore.microservices.loginservice.services.impl;

import com.ebookstore.microservices.loginservice.exceptions.FacebookUserLoginException;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import com.ebookstore.microservices.loginservice.security.services.UserDetailsImpl;
import com.ebookstore.microservices.loginservice.client.FacebookClient;
import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.models.facebook.FacebookUser;
import com.ebookstore.microservices.loginservice.security.jwt.JwtUtils;
import com.ebookstore.microservices.loginservice.services.FacebookService;
import com.ebookstore.microservices.loginservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class FacebookServiceImpl implements FacebookService {

    @Autowired
    private FacebookClient facebookClient;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse loginUser(String fbAccessToken) {
        FacebookUser facebookUser = facebookClient.getUser(fbAccessToken);

        String jwtToken = userService.findUserByFacebookId(facebookUser.getId())
                .or(() -> Optional.ofNullable(userService.registerUser(convertTo(facebookUser), String.valueOf(Roles.ROLE_USER))))
                .map(UserDetailsImpl::build)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(jwtUtils::generateJwtToken)
                .orElseThrow(() ->
                        new FacebookUserLoginException("unable to login facebook user id " + facebookUser.getId()));
        User user = userService.findUserByFacebookId(facebookUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with this username does not exist!"));

        return new JwtResponse(jwtToken, user.getUsername(), user.getEmail(), user.getRole().toString(), user.getUserId());
    }

    @Override
    public User convertTo(FacebookUser facebookUser) {
        return new User(generateUsername(facebookUser.getName()), facebookUser.getEmail(), generatePassword(8), facebookUser.getId(), null);
    }

    @Override
    public String generateUsername(String username) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%s.%06d", username, number);
    }

    @Override
    public String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
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
