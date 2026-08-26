package com.EDebugger.booking_logic.controllers;

import com.EDebugger.booking_logic.enums.ServiceType;
import com.EDebugger.booking_logic.dtos.RestaurantPresentationForUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.EDebugger.booking_logic.repositories.RestaurantRepository;
import com.EDebugger.booking_logic.services.RestaurantService;

import java.util.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @GetMapping("/getAll") // called immediately on main page?
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getAll() { // if response is ok
        return ResponseEntity.ok(restaurantService.getAll()); // will return a list
    }

     // when user clicks card, activate this
    @GetMapping("/getById/{id}") // for frontend use?
    public @ResponseBody ResponseEntity<Optional<RestaurantPresentationForUser>> getById(@PathVariable(value = "id") Long id ) {
        return ResponseEntity.of(Optional.ofNullable(restaurantService.getById(id)));
    } //gets by id

    // get by restaurant name
    @GetMapping("/getByName/{name}")
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.of(Optional.ofNullable((restaurantService.getByName(name))));
    }

     // explicitly meant for user during filtration
    @GetMapping("/getByServiceType/{serviceType}")
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByServiceType(@PathVariable(value = "serviceType") ServiceType serviceType) {
        return ResponseEntity.ok(restaurantService.getByServiceType(serviceType));
    }

     // explicitly meant for user during filtration
    @GetMapping("/getPriceRangeWithin/{num}") // get a price matching or below it
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getPriceRangeWithin(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantService.getByPriceWithin(num)));
    }

     // explicitly meant for user during filtration
    @GetMapping("/getPriceRangeBeyond/{num}") // same as above but opposite end
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getPriceRangeBeyond(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantService.getByPriceBeyond(num)));
    }

     // explicitly meant for user during filtration
    @GetMapping("/getByRating/{num}") // same as above but opposite end
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByRating(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantService.getByRating(num)));
    }

     // explicitly meant for user during filtration
    @GetMapping("/getByCompany/{com}")
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByCompany(@PathVariable(value = "com") String com) {
        return ResponseEntity.ok(restaurantService.getByCompany(com));
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    // what to put in...

    /*---------------------------Setters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurantById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Restaurant deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    // only considering...
//    @PostMapping("/postRestaurant/{restaurant}")
//    public ResponseEntity<?> postRestaurant(@Valid @RequestBody )

    /*---------------------------Posters------------------------------------------*/
    /*----------------------------------------------------------------------------*/

}
