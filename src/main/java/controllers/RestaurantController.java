package controllers;

import enums.ServiceType;
import models.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.RestaurantRepository;

import java.util.*;

@RestController
@RequestMapping("/Restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Restaurant>> getAll() { // if response is ok
        return ResponseEntity.ok(restaurantRepository.findAll()); // will return a list
    }

    @GetMapping("/getById{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id ) {
        return ResponseEntity.of(restaurantRepository.findById(id));
    } //gets by id

    @GetMapping("/getByName{name}")
    //@Query("SELECT * FROM restaurants WHERE name=?")
    public ResponseEntity<Restaurant> getByName(@PathVariable String name) {
        return ResponseEntity.ok(restaurantRepository.findByName(name));
    }

    @GetMapping("/getByServiceType{serviceType}")
    public ResponseEntity<Restaurant> getByServiceType(@PathVariable ServiceType serviceType) {
        return ResponseEntity.ok(restaurantRepository.findByServiceType(serviceType));
    }

}
