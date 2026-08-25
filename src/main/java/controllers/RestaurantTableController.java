package controllers;

import dtos.RestaurantTableDTO;  // You need to create this DTO
import jakarta.validation.Valid;
import models.RestaurantTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import services.RestaurantTableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurants/{restaurantId}/tables")  // FIXED: Added restaurantId to base path
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    public RestaurantTableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }

    /*---------------------------GETTERS------------------------------------------*/

    // Get all tables for a restaurant (public info)
    @GetMapping  // FIXED: Removed /getAll, using root path
    public ResponseEntity<List<RestaurantTableDTO>> getAllTables(
            @PathVariable Long restaurantId) {
        List<RestaurantTableDTO> tables = tableService.getTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }

    // Get available tables only (for booking)
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/available")  // FIXED: Better naming
    public ResponseEntity<List<RestaurantTableDTO>> getAvailableTables(
            @PathVariable Long restaurantId) {
        List<RestaurantTableDTO> tables = tableService.getAvailableTablesByRestaurant(restaurantId);
        return ResponseEntity.ok(tables);
    }

    // Find best table for a party size
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/best-fit")
    public ResponseEntity<?> findBestTable(
            @PathVariable Long restaurantId,
            @RequestParam Integer partySize) {

        if (partySize == null || partySize <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Party size must be greater than 0"));
        }

        Optional<RestaurantTableDTO> table =
                tableService.findBestTableForParty(restaurantId, partySize);

        if (table.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No available tables for party size: " + partySize));
        }

        return ResponseEntity.ok(table.get());
    }

    // Get tables by section
    @GetMapping("/section/{section}")
    public ResponseEntity<List<RestaurantTableDTO>> getTablesBySection(
            @PathVariable Long restaurantId,
            @PathVariable String section) {
        List<RestaurantTableDTO> tables =
                tableService.getTablesBySection(restaurantId, section);
        return ResponseEntity.ok(tables);
    }

    // Get all sections in a restaurant
    @GetMapping("/sections")
    public ResponseEntity<List<String>> getSections(
            @PathVariable Long restaurantId) {
        List<String> sections = tableService.getSectionsByRestaurant(restaurantId);
        return ResponseEntity.ok(sections);
    }

    // Get available table count (for "Available" checkbox filter on main page)
    @GetMapping("/available/count")
    public ResponseEntity<Map<String, Long>> countAvailableTables(
            @PathVariable Long restaurantId) {
        long count = tableService.countAvailableTables(restaurantId);
        return ResponseEntity.ok(Map.of("availableCount", count));
    }

    /*---------------------------GETTERS------------------------------------------*/
    /*---------------------------SETTERS------------------------------------------*/

    // Mark table as available/unavailable (Admin only)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PutMapping("/{tableId}/availability")
    public ResponseEntity<RestaurantTableDTO> updateTableAvailability(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId,
            @RequestParam Boolean available) {
        try {
            RestaurantTableDTO updated = tableService.updateTableAvailability(tableId, available);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Create a new table (Admin only)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantTableDTO> createTable(
            @PathVariable Long restaurantId,
            @Valid @RequestBody RestaurantTableDTO tableDTO) {
        try {
            RestaurantTableDTO created = tableService.createTable(restaurantId, tableDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update table (Admin only)
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
//    @PutMapping("/{tableId}")
//    public ResponseEntity<RestaurantTableDTO> updateTable(
//            @PathVariable Long restaurantId,
//            @PathVariable Long tableId,
//            @Valid @RequestBody RestaurantTableDTO tableDTO) {
//        try {
//            RestaurantTableDTO updated = tableService.updateTable(tableId, tableDTO);
//            return ResponseEntity.ok(updated);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

    // Delete a table (SuperAdmin only)
    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/{tableId}")
    public ResponseEntity<String> deleteTable(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId) {
        try {
            tableService.deleteTable(tableId,restaurantId);
            return ResponseEntity.ok("Table deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    /*---------------------------SETTERS------------------------------------------*/
}
