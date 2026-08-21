package controllers;

import dtos.CreateUserDto;
import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import models.User;
import enums.Role;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repositories.RestaurantRepository;
import repositories.UserRepository;
import services.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
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
