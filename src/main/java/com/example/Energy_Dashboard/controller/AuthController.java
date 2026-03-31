package com.example.Energy_Dashboard.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.Energy_Dashboard.model.User;
import com.example.Energy_Dashboard.repository.UserRepository;
import com.example.Energy_Dashboard.security.JwtService;
import com.example.Energy_Dashboard.service.dto.AuthResponse;
import com.example.Energy_Dashboard.service.dto.LoginRequest;
import com.example.Energy_Dashboard.service.dto.SignupRequest;
import com.example.Energy_Dashboard.service.dto.SignupResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, 
                         UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.findByEmailId(request.getEmailId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmailId(request.getEmailId());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(com.example.Energy_Dashboard.model.Role.USER);

        User savedUser = userRepository.save(user);

        return new SignupResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmailId(),
            savedUser.getRole().name(),
            "User registered successfully"
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        String token = jwtService.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal(),
                Map.of("role", role));

        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs(), request.getUsername(), role);
    }
}
