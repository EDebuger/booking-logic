package controllers;

import models.RestaurantTable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import services.RestaurantTableService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tables")
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    public RestaurantTableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }


     // Get all tables for a restaurant.
    @PreAuthorize("hasAnyRole()")
    @GetMapping("/getAll")
    public ResponseEntity<List<RestaurantTable>> getAllTables(
            @PathVariable Long restaurantId) {
        List<RestaurantTable> tables = tableService.getTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }


     // Get available tables only.
    @PreAuthorize("hasRole('USER')") // for filtration
    @GetMapping("/getAllAvailable")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables(
            @PathVariable Long restaurantId) {
        List<RestaurantTable> tables = tableService.getAvailableTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }

     // Find best table for a party size.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/best-fit")
    public ResponseEntity<?> findBestTable(
            @PathVariable Long restaurantId,
            @RequestParam Integer partySize) {
        Optional<RestaurantTable> table =
                tableService.findBestTableForParty(restaurantId, partySize);

        if (table.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new Exception("No available tables for party size: " + partySize));
        }

        return ResponseEntity.ok(table.get());
    }


     // Get tables by section.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/section/{section}")
    public ResponseEntity<List<RestaurantTable>> getTablesBySection(
            @PathVariable Long restaurantId,
            @PathVariable String section) {
        List<RestaurantTable> tables =
                tableService.getTablesBySection(restaurantId, section);
        return ResponseEntity.ok(tables);
    }


     // Get all sections.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sections")
    public ResponseEntity<List<String>> getSections(
            @PathVariable Long restaurantId) {
        List<String> sections = tableService.getSectionsByRestaurant(restaurantId);
        return ResponseEntity.ok(sections);
    }

     // Get available table count.
    @PreAuthorize("hasAnyRole()")
    @GetMapping("/available/count")
    public ResponseEntity<Long> countAvailableTables(
            @PathVariable Long restaurantId) {
        long count = tableService.countAvailableTables(restaurantId);
        return ResponseEntity.ok(count);
    }
}

