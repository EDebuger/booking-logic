package com.EDebugger.booking_logic.controllers;

import com.EDebugger.booking_logic.dtos.CreateUserDto;
import com.EDebugger.booking_logic.dtos.LoginDto;
import com.EDebugger.booking_logic.dtos.UserProfileforAdmin;
import com.EDebugger.booking_logic.dtos.UserProfileforUser;
import com.EDebugger.booking_logic.enums.Role;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.EDebugger.booking_logic.services.JwtService;
import com.EDebugger.booking_logic.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Controller
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
        try {
            user.setUserRole(Role.USER); // default;
            UserProfileforUser savedUser = userService.createUser(user);
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
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<?> whoAmI(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetailsService.loadUserByUsername(userDetails.getUsername()));
    }

    @GetMapping("/testGetUsers")
    public ResponseEntity<List<UserProfileforAdmin>> testGet() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/testRequest")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }

    // Page redirects
    @GetMapping("/main")
    public String getMainPage() {
        return "redirect:/main.html";
    }

    @GetMapping("/details")
    public String getDetailPage() {
        return "redirect:/details.html";
    }

    @GetMapping("/profile")
    public String getProfilePage() {
        return "redirect:/profile.html";
    }
}
