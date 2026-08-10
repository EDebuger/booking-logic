package controllers;

import enums.ServiceType;
import models.Restaurant;
import dtos.RestaurantPresentationForUser;
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

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/
    @GetMapping("/getAll")
    public @ResponseBody ResponseEntity<List<Restaurant>> getAll() { // if response is ok
        return ResponseEntity.ok(restaurantRepository.findAll()); // will return a list
    }

    @GetMapping("/getById{id}")
    public @ResponseBody ResponseEntity<Restaurant> getById(@PathVariable(value = "id") Long id ) {
        return ResponseEntity.of(restaurantRepository.findById(id));
    } //gets by id

    @GetMapping("/getByName{name}")
    @Query(value = "SELECT * FROM restaurants WHERE name LIKE :name", nativeQuery = true)
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.of(Optional.ofNullable((restaurantRepository.findByName(name))));
    }

    @GetMapping("/getByServiceType{serviceType}")
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByServiceType(@PathVariable(value = "serviceType") ServiceType serviceType) {
        return ResponseEntity.ok(restaurantRepository.findByServiceType(serviceType));
    }

    @GetMapping("/getPriceRangeWithin{num}") // get a price matching or below it
    @Query(value = "SELECT * FROM restaurants WHERE price_range = :num OR price_range < :num", nativeQuery = true)
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getPriceRangeWithin(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantRepository.findRestaurantByPriceRangeWithin(num)));
    }

    @GetMapping("/getPriceRangeBeyond{num}") // same as above but opposite end
    @Query(value = "SELECT * FROM restaurants WHERE price_range = :num OR price_range > :num", nativeQuery = true)
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getPriceRangeBeyond(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantRepository.findRestaurantByPriceRangeBeyond(num)));
    }

    @GetMapping("/getByCompany{com}")
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByCompany(@PathVariable(value = "com") String com) {
        return ResponseEntity.ok(restaurantRepository.findRestaurantBysubOf(com));
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    // what to put in...

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

}
