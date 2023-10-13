package com.ebookstore.microservices.loginservice.web;


import com.ebookstore.microservices.loginservice.dto.UserDto;
import com.ebookstore.microservices.loginservice.exceptions.InvalidOrExpiredTokenException;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.payload.requests.LoginRequest;
import com.ebookstore.microservices.loginservice.payload.requests.RegisterRequest;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import com.ebookstore.microservices.loginservice.payload.responses.MessageResponse;
import com.ebookstore.microservices.loginservice.repositories.RoleRepository;
import com.ebookstore.microservices.loginservice.services.FacebookService;
import com.ebookstore.microservices.loginservice.services.GoogleService;
import com.ebookstore.microservices.loginservice.services.RoleService;
import com.ebookstore.microservices.loginservice.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    FacebookService facebookService;

    @Autowired
    GoogleService googleService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
        userService.registerUser(user, registerRequest.getRole().toString());
        String siteURL = getSiteURL(request);
        userService.sendVerificationEmail(user, siteURL);

        return ResponseEntity.ok("User registered successfully! CHeck your email to verify the account.");
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String code){
        boolean verified = userService.verify(code);
        return verified ? "Verification success!" : "Verification failed!";
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/facebook/signin")
    public  ResponseEntity<JwtResponse> facebookAuth(@RequestParam String facebookAccessToken) {
        JwtResponse jwtResponse = facebookService.loginUser(facebookAccessToken);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/google/signin")
    public ResponseEntity<JwtResponse> googleAuth(@RequestParam String googleAccessToken){
        JwtResponse jwtResponse = googleService.loginUser(googleAccessToken);
        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String tokenHeader) {

        User user = userService.validateToken(tokenHeader);
        if(user != null) {
            UserDto userDto = new UserDto(user.getUserId(), user.getRole().getName());
            return ResponseEntity.ok(userDto);
        }
        else
            throw new InvalidOrExpiredTokenException("Token is invalid or expired.");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
