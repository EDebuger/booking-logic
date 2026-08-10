package controllers;

import dtos.UserProfileforAdmin;
import jakarta.transaction.Transactional;
import models.User;
import enums.Role;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getAll") // every single one, even admins
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    @GetMapping("/getById{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }
    @GetMapping("/getByName{name}") // to be used by admins
    public ResponseEntity<User> getByName(@PathVariable String name) {
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
    @PostMapping(value = "/insertUser")
    public ResponseEntity<Object> insertUser(@RequestBody User user) {
        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/


    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }


    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

}
