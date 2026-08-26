package com.EDebugger.booking_logic.controllers;

import com.EDebugger.booking_logic.dtos.ChangeUserDto;
import com.EDebugger.booking_logic.dtos.CreateUserDto;
import com.EDebugger.booking_logic.dtos.UserProfileforAdmin;
import com.EDebugger.booking_logic.dtos.UserProfileforUser;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import com.EDebugger.booking_logic.models.User;
import com.EDebugger.booking_logic.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.EDebugger.booking_logic.repositories.UserRepository;
import com.EDebugger.booking_logic.services.PasswordEncoderService;
import com.EDebugger.booking_logic.services.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoderService passwordEncoder;

    public UserController(UserRepository userRepository, UserService userService, PasswordEncoderService passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/getAll") // every single one, even admins
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/getById/{id}") // for user
    public ResponseEntity<UserProfileforUser> getById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/getByName/{name}") // to be used by admins
    public ResponseEntity<User> getByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(userRepository.findByUserName(name));
    }

    @GetMapping("/getAllUsers")
    public  List<UserProfileforAdmin> getAllUsers() {
        return userService.getAllUsers(); // retrieve from service
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PostMapping("/users/{id}/validate-phone")
    public HttpStatusCode validatePhoneChange(@PathVariable Long id, @Valid @RequestBody ChangeUserDto.ChangePhoneValidationDto dto) {
        return ResponseEntity.ok(userService.validatePhoneChange(
                id, dto.getPhone(), dto.getCurrentPassword())).getStatusCode();
    }

    @PostMapping("/users/{id}/validate-username")
    public HttpStatusCode validateUsernameChange(@PathVariable Long id, @Valid @RequestBody ChangeUserDto.ChangeUsernameValidationDto dto) {
        return ResponseEntity.ok(userService.validateUsernameChange(
                id, dto.getNewUsername(), dto.getCurrentPassword())).getStatusCode();
    }

    @PostMapping("/users/{id}/validate-password")
    public HttpStatusCode validatePasswordChange(@PathVariable Long id, @Valid @RequestBody ChangeUserDto.ChangePasswordValidationDto dto) {
        // Calls service validation
        try {
            passwordEncoder.encodePassword(dto.getCurrentPassword());
            return ResponseEntity.ok(userService.validatePasswordChange(
                id, dto.getNewPassword(), dto.getCurrentPassword())).getStatusCode();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()+"Could not get password changed");
        }
    }

    @PutMapping("/users/{id}/profile-changes")
    @Transactional
    public HttpStatusCode applyProfileChanges(@PathVariable Long id, @Valid @RequestBody ChangeUserDto.ApplyProfileChangesDto dto) {
        // Calls service to apply all changes
        return ResponseEntity.ok(userService.applyProfileChanges(
                id, dto.getPhone(), dto.getUsername(), dto.getNewPassword())).getStatusCode();
    }


    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/


    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @DeleteMapping("/deleteUser/{id}/{role}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @PathVariable Role role) {
        try {
            userService.deleteUserById(id,role);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/deleteAdmin/{id}/{role}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id, @PathVariable Role role) {
        try {
            userService.deleteAdminById(id,role);
            return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }


    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/


    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    // useful for newcomers
    @PostMapping(value = "/insertUser")
    public ResponseEntity<Object> insertUser(@Valid @RequestBody() CreateUserDto user) {
        try { UserProfileforUser savedUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

}
