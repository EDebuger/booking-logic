package controllers;

import models.User;
import enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.RestaurantRepository;
import repositories.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/Users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    @GetMapping("/getById{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }
    @GetMapping("/getByName{name}")
    public ResponseEntity<User> getByName(@PathVariable String name) {
        return ResponseEntity.ok(userRepository.findByUserName(name));
    }

}
