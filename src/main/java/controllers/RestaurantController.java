package controllers;

import enums.ServiceType;
import dtos.RestaurantPresentationForUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.RestaurantRepository;
import services.RestaurantService;

import java.util.*;

@RestController
@RequestMapping("/Restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }

    /*---------------------------Getters------------------------------------------*/
    /*----------------------------------------------------------------------------*/
    @GetMapping("/getAll") // called immediately on main page?
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getAll() { // if response is ok
        return ResponseEntity.ok(restaurantService.getAll()); // will return a list
    }

    @GetMapping("/getById/{id}") // for frontend use?
    public @ResponseBody ResponseEntity<Optional<RestaurantPresentationForUser>> getById(@PathVariable(value = "id") Long id ) {
        return ResponseEntity.of(Optional.ofNullable(restaurantService.getById(id)));
    } //gets by id

    @GetMapping("/getByName/{name}")
    @Query(value = "SELECT * FROM restaurants WHERE name LIKE :name", nativeQuery = true)
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByName(@PathVariable(value = "name") String name) {
        return ResponseEntity.of(Optional.ofNullable((restaurantRepository.findByName(name))));
    }

    @GetMapping("/getByServiceType/{serviceType}")
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getByServiceType(@PathVariable(value = "serviceType") ServiceType serviceType) {
        return ResponseEntity.ok(restaurantRepository.findByServiceType(serviceType));
    }

    @GetMapping("/getPriceRangeWithin/{num}") // get a price matching or below it
    @Query(value = "SELECT * FROM restaurants WHERE price_range = :num OR price_range < :num", nativeQuery = true)
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getPriceRangeWithin(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantRepository.findRestaurantByPriceRangeWithin(num)));
    }

    @GetMapping("/getPriceRangeBeyond/{num}") // same as above but opposite end
    @Query(value = "SELECT * FROM restaurants WHERE price_range = :num OR price_range > :num", nativeQuery = true)
    public @ResponseBody ResponseEntity<List<RestaurantPresentationForUser>> getPriceRangeBeyond(@PathVariable(value = "num") int num) {
        return ResponseEntity.of(Optional.ofNullable(restaurantRepository.findRestaurantByPriceRangeBeyond(num)));
    }

    @GetMapping("/getByCompany/{com}")
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

    /*---------------------------Deleters-----------------------------------------*/
    /*----------------------------------------------------------------------------*/

    @DeleteMapping("/deleteById{id}")
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

}
