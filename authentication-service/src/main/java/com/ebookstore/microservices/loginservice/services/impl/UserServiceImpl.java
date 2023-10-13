package com.ebookstore.microservices.loginservice.services.impl;

import com.ebookstore.microservices.loginservice.enumerations.Roles;
import com.ebookstore.microservices.loginservice.exceptions.EmailAlreadyUsedException;
import com.ebookstore.microservices.loginservice.exceptions.UsernameAlreadyUsedException;
import com.ebookstore.microservices.loginservice.models.Role;
import com.ebookstore.microservices.loginservice.models.User;
import com.ebookstore.microservices.loginservice.payload.responses.JwtResponse;
import com.ebookstore.microservices.loginservice.repositories.UserRepository;
import com.ebookstore.microservices.loginservice.security.jwt.JwtUtils;
import com.ebookstore.microservices.loginservice.security.services.UserDetailsImpl;
import com.ebookstore.microservices.loginservice.services.RoleService;
import com.ebookstore.microservices.loginservice.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleService roleService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JavaMailSender mailSender;

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public User registerUser(User user, String strRole) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyUsedException("Username is already taken!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyUsedException("Email is already taken!");
        }

        Role role;

        if(Objects.equals(strRole, Roles.ROLE_ADMIN.toString()))
            role = roleService.findRoleByName((Roles.ROLE_ADMIN));
        else
            role = roleService.findRoleByName(Roles.ROLE_USER);

        user.setRole(role);

        String randomCode = RandomStringUtils.random(64, true, true);
        user.setVerificationCode(randomCode);

        String username = user.getUsername();
        String email = user.getEmail();
        String password = passwordEncoder.encode(user.getPassword());
        String verificationCode = user.getVerificationCode();
        String facebookId = user.getFacebookId();
        String googleId = user.getGoogleId();

        if(facebookId != null)
            return userRepository.save(new User(username, email, password, role, verificationCode, facebookId, null));
        else if(googleId != null)
            return userRepository.save(new User(username, email, password, role, verificationCode, null, googleId));
        else
            return userRepository.save(new User(username, email, password, role, verificationCode));
    }

    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "E-book store";
        String verifyURL = siteURL + "/auth/verify?code=" + user.getVerificationCode();

        String mailContent = "<p>Dear " + user.getUsername() +" </p>"
                + "<p>Please click the link below to verify your registration:</p>"
                + "<h3><a href="+ verifyURL +">VERIFY</a></h3>"
                + "<p>Thank you,<br>E-book store</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("pavel.kitanov.oo@gmail.com", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);

        mailSender.send(message);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public JwtResponse loginUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst().get();

        User user = userRepository.findUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with this username does not exist!"));

        return new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                role,
                user.getUserId());
    }


    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Optional<User> findUserByFacebookId(String facebookId) {
        return userRepository.findUserByFacebookId(facebookId);
    }

    @Override
    public Optional<User> findUserByGoogleId(String googleId) {
        return userRepository.findUserByGoogleId(googleId);
    }

    @Override
    public boolean verify(String verificationCode) {
        User user = userRepository.findUserByVerificationCode(verificationCode);
        if(user == null || user.getEnabled())
            return false;
        else{
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public User validateToken(String tokenHeader) {
        String token = extractTokenFromHeader(tokenHeader);

        // Validate the token
        boolean isValid = jwtUtils.validateJwtToken(token);

        if (isValid) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with this username does not exist!"));
            return user;
        } else {
            return null;
        }
    }

    private String extractTokenFromHeader(String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        }
        return null;
    }


}
