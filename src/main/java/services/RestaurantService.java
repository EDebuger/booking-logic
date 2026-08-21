package services;

import dtos.RestaurantPresentationForUser;
import dtos.UserProfileforUser;
import enums.ServiceType;
import jakarta.transaction.Transactional;
import models.Restaurant;
import models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import repositories.RestaurantRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public @ResponseBody List<RestaurantPresentationForUser> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<RestaurantPresentationForUser> getByPriceBeyond(int p) {
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByPriceRangeBeyond(p);
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }
    public @ResponseBody List<RestaurantPresentationForUser> getByPriceWithin(int p) {
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByPriceRangeWithin(p);
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<RestaurantPresentationForUser> getByName(String name) {
        List<Restaurant> restaurants = restaurantRepository.findByName(name);
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<RestaurantPresentationForUser> getByRating(int p) {
        List<Restaurant> restaurants = restaurantRepository.findRestaurantByRating(p);
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<RestaurantPresentationForUser> getByServiceType(ServiceType s) {
        List<Restaurant> restaurants = restaurantRepository.findByServiceType(s);
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody List<RestaurantPresentationForUser> getByCompany(String n) {
        List<Restaurant> restaurants = restaurantRepository.findRestaurantBysubOf(n);
        return restaurants.stream()
                .map(this::convertToRestaurantUserDTO)
                .collect(Collectors.toList());
    }

    public @ResponseBody Optional<RestaurantPresentationForUser> getById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id); // if none
        return restaurant.map(this::convertToRestaurantUserDTO);
    }

    @Transactional
    public void deleteRestaurantById(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RuntimeException("Restaurant not found with ID: " + id);
        } // when will it actually need to
        restaurantRepository.deleteById(id);
    }

    private RestaurantPresentationForUser convertToRestaurantUserDTO(Restaurant restaurant) {
        RestaurantPresentationForUser dto = new RestaurantPresentationForUser( //dto without setters
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAdress(),
                restaurant.getPostalCode(),
                restaurant.getServiceType(),
                restaurant.getDescription(),
                restaurant.getPriceRange(),
                restaurant.getRating(),
                restaurant.getImage(),
                restaurant.getSubOf()
        );
        // Map only the fields you need for the admin profile
        return dto;
    }
}
