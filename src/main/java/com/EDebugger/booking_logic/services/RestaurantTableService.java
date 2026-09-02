package com.EDebugger.booking_logic.services;

import com.EDebugger.booking_logic.dtos.RestaurantTableDTO;
import com.EDebugger.booking_logic.models.Restaurant;
import com.EDebugger.booking_logic.models.RestaurantTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.EDebugger.booking_logic.repositories.RestaurantRepository;
import com.EDebugger.booking_logic.repositories.RestaurantTableRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RestaurantTableService {

    private final RestaurantTableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantTableService(RestaurantTableRepository tableRepository, RestaurantRepository restaurantRepository) {
        this.tableRepository = tableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Get all tables for a restaurant.
     */
    public List<RestaurantTableDTO> getTablesByRestaurant(Long restaurantId) {
        System.out.println("Fetching all tables for restaurant: {}"+ restaurantId);
        List<RestaurantTable> tables = tableRepository.findByRestaurantIdOrderByTableNumber(restaurantId);
        return tables.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get available tables for a restaurant.
     */
    public List<RestaurantTableDTO> getAvailableTablesByRestaurant(Long restaurantId) {
        System.out.println("Fetching available tables for restaurant: {}"+ restaurantId);
        List<RestaurantTable> tables = tableRepository.findAvailableTablesByRestaurant(restaurantId);
        return tables.stream().map(this::toDTO).collect(Collectors.toList());
    }

     // Find the best table based on party size.
    public Optional<RestaurantTableDTO> findBestTableForParty(Long restaurantId, Integer partySize, Date date) {
        System.out.println("Finding best table for party of "+partySize+" at restaurant: "+restaurantId);

        List<RestaurantTable> availableTables =
                tableRepository.findAvailableTablesByCapacity(restaurantId, partySize, date);

        if (availableTables.isEmpty()) {
            System.out.println("No available tables for party size {} at restaurant {}"+ partySize+ restaurantId);
            return Optional.empty();
        }
         List<RestaurantTableDTO> tables = availableTables.stream().map(this::toDTO).collect(Collectors.toList());
        // Return the smallest table that fits the party (best fit algorithm)
        return tables.stream().findFirst();
    }

     // Get tables by section.
    public List<RestaurantTableDTO> getTablesBySection(Long restaurantId, String section) {
        System.out.println("Fetching tables in section '{}' for restaurant: {}"+ section+ restaurantId);
        List<RestaurantTable> tables = tableRepository.findTablesByRestaurantAndSection(restaurantId, section);
        return tables.stream().map(this::toDTO).collect(Collectors.toList());
    }

     // Get all sections in a restaurant.
    public List<String> getSectionsByRestaurant(Long restaurantId) {
        System.out.println("Fetching sections for restaurant: {}"+ restaurantId);
        return tableRepository.findDistinctSectionsByRestaurant(restaurantId);
    }

    @Transactional
    public RestaurantTableDTO createTable(Long restaurantId, RestaurantTableDTO tableDTO) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restaurantId));

        RestaurantTable table = new RestaurantTable();
        table.setRestaurant(restaurant);
        table.setTableNumber(tableDTO.getTableNumber());
        table.setSection(tableDTO.getSection());
        table.setCapacity(tableDTO.getCapacity());
        table.setAvailable(tableDTO.getIsAvailable());

        RestaurantTable created = tableRepository.save(table);

        return toDTO(created);
    }

    @Transactional
    public void deleteTable(Long restaurantId, Long tableId) {
        if(tableRepository.existsById(tableId) && tableRepository.existsById(restaurantId)) {
            tableRepository.deleteById(tableId);
        }
            else{throw new IllegalArgumentException("Restaurant or table was not found with id: ");}
    }



    // Mark a table as available.
    @Transactional
    public RestaurantTable markTableAvailable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));
        table.markAvailable();
        System.out.println("Marked table {} as available"+ tableId);
        return tableRepository.save(table);
    }

     // Mark a table as unavailable.
    @Transactional
    public RestaurantTable markTableUnavailable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

        table.markUnavailable();
        System.out.println("Marked table {} as unavailable"+ tableId);
        return tableRepository.save(table);
    }

    @Transactional
    public RestaurantTableDTO updateTableAvailability(Long tableId, Boolean available) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + tableId));

        table.setAvailable(available);
        RestaurantTable updated = tableRepository.save(table);

        return toDTO(updated);
    }


    // Count available tables.
    public long countAvailableTables(Long restaurantId) {
        return tableRepository.countAvailableTablesByRestaurant(restaurantId);
    }

    // In RestaurantTableService
    public RestaurantTableDTO toDTO(RestaurantTable entity) {
        if (entity == null) return null;

        return new RestaurantTableDTO(
                entity.getId(),
                entity.getRestaurant().getId(),
                entity.getTableNumber(),
                entity.getSection(),
                entity.getCapacity(),
                entity.getAvailable(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public RestaurantTable toEntity(RestaurantTableDTO dto, Restaurant restaurant) {
        if (dto == null) return null;

        RestaurantTable entity = new RestaurantTable();
        entity.setId(dto.getId());
        entity.setRestaurant(restaurant);
        entity.setTableNumber(dto.getTableNumber());
        entity.setSection(dto.getSection());
        entity.setCapacity(dto.getCapacity());
        entity.setAvailable(dto.getIsAvailable());

        return entity;
    }

}
