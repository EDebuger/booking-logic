package services;

import models.RestaurantTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.RestaurantTableRepository;

import java.util.List;
import java.util.Optional;

import static jdk.internal.org.jline.utils.Log.*;

@Service
public class RestaurantTableService {

    private final RestaurantTableRepository tableRepository;

    public RestaurantTableService(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * Get all tables for a restaurant.
     */
    public List<RestaurantTable> getTablesByRestaurant(Long restaurantId) {
        info("Fetching all tables for restaurant: {}", restaurantId);
        return tableRepository.findByRestaurantIdOrderByTableNumber(restaurantId);
    }

    /**
     * Get available tables for a restaurant.
     */
    public List<RestaurantTable> getAvailableTablesByRestaurant(Long restaurantId) {
        info("Fetching available tables for restaurant: {}", restaurantId);
        return tableRepository.findAvailableTablesByRestaurant(restaurantId);
    }

     // Find the best table based on party size.
    public Optional<RestaurantTable> findBestTableForParty(Long restaurantId, Integer partySize) {
        info("Finding best table for party of {} at restaurant: {}", partySize, restaurantId);

        List<RestaurantTable> availableTables =
                tableRepository.findAvailableTablesByCapacity(restaurantId, partySize);

        if (availableTables.isEmpty()) {
            warn("No available tables for party size {} at restaurant {}",
                    partySize, restaurantId);
            return Optional.empty();
        }

        // Return the smallest table that fits the party (best fit algorithm)
        return availableTables.stream().findFirst();
    }

     // Get tables by section.
    public List<RestaurantTable> getTablesBySection(Long restaurantId, String section) {
        info("Fetching tables in section '{}' for restaurant: {}", section, restaurantId);
        return tableRepository.findTablesByRestaurantAndSection(restaurantId, section);
    }

     // Get all sections in a restaurant.
    public List<String> getSectionsByRestaurant(Long restaurantId) {
        info("Fetching sections for restaurant: {}", restaurantId);
        return tableRepository.findDistinctSectionsByRestaurant(restaurantId);
    }

     // Mark a table as available.
    @Transactional
    public RestaurantTable markTableAvailable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

        table.markAvailable();
        info("Marked table {} as available", tableId);
        return tableRepository.save(table);
    }

     // Mark a table as unavailable.
    @Transactional
    public RestaurantTable markTableUnavailable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

        table.markUnavailable();
        info("Marked table {} as unavailable", tableId);
        return tableRepository.save(table);
    }

     // Count available tables.
    public long countAvailableTables(Long restaurantId) {
        return tableRepository.countAvailableTablesByRestaurant(restaurantId);
    }
}
