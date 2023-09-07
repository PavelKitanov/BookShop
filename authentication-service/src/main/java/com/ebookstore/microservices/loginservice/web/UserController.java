package com.ebookstore.microservices.loginservice.web;

import com.ebookstore.microservices.loginservice.exceptions.EmailAlreadyUsed;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    public User registerUser(@RequestBody User user){
        User existingUser = userService.findUserByEmail(user.getEmail());
        if(existingUser != null){
            throw new EmailAlreadyUsed("There is a user already registered with this email: " + user.getEmail(), HttpStatus.CONFLICT);
        }
        return userService.saveUser(user);
    }

    @PostMapping("/loginUser")
    public User loginUser(@RequestParam String email,
                          @RequestParam String password){
        return userService.loginUser(email, password);
    }
}
