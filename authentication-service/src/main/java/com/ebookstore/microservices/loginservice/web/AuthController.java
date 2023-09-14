package com.ebookstore.microservices.loginservice.web;


import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.payload.requests.LoginRequest;
import com.ebookstore.microservices.loginservice.payload.requests.RegisterRequest;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import com.ebookstore.microservices.loginservice.payload.responses.MessageResponse;
import com.ebookstore.microservices.loginservice.repositories.RoleRepository;
import com.ebookstore.microservices.loginservice.services.FacebookService;
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


    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
        userService.registerUser(user, registerRequest.getRole().toString());
        String siteURL = getSiteURL(request);
        userService.sendVerificationEmail(user, siteURL);

        return ResponseEntity.ok(new MessageResponse("User registered successfully! CHeck your email to verify the account."));
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String code){
        boolean verified = userService.verify(code);
        return verified ? "Verification success!" : "Verification failed!";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/facebook/signin")
    public  ResponseEntity<?> facebookAuth(@RequestParam String facebookAccessToken) {
        JwtResponse JwtResponse = facebookService.loginUser(facebookAccessToken);
        return ResponseEntity.ok(JwtResponse);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String tokenHeader) {

        User user = userService.validateToken(tokenHeader);
        if(user != null)
            return ResponseEntity.ok(user.getUserId().toString());
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
