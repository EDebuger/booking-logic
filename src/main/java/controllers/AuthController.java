package controllers;

import dtos.CreateUserDto;
import dtos.LoginDto;
import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import enums.Role;
import jakarta.validation.Valid;
import models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import services.JwtService;
import services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> insertUser(@Valid @RequestBody() CreateUserDto user) {
        try { passwordEncoder.encode(user.getPassword()); //encrypted
            user.setUserRole(Role.USER); // default
            user.setMemberSince(LocalDate.now()); // current
            UserProfileforUser savedUser = userService.createUser(user); // what we get back
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserName(),dto.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUserName());
        String token = jwtService.generateToken(userDetails); // token is tied to user
        return  ResponseEntity.ok(token); // you get the token
    }

    @GetMapping("/me")
    public ResponseEntity<String> whoAmI(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @GetMapping("/testGetUsers")
    public ResponseEntity<List<UserProfileforAdmin>> testGet() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
