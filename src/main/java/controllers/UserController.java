package controllers;

import dtos.UserProfileforAdmin;
import models.User;
import enums.Role;
import org.springframework.data.jpa.repository.Modifying;
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
    @GetMapping("/getByName{name}")
    public ResponseEntity<User> getByName(@PathVariable String name) {
        return ResponseEntity.ok(userRepository.findByUserName(name));
    }
    @GetMapping("/getAllUsers")
    public @ResponseBody ResponseEntity<List<UserProfileforAdmin>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findByUserRole(Role.USER));
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    //@Modifying("/insertUser{userName,email,phone,userRole}")
    //@Query(value = "INSERT INTO users (user_name,email,phone,user_role,member_since,updated_at) VALUES(:userName,:email,:phone,:userRole,CURRENT DATE ,CURRENT DATE );")

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

}
