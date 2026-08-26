package com.EDebugger.booking_logic.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class RestaurantTableDTO {

    private Long id;

    @NotNull(message = "Restaurant ID cannot be null")
    private Long restaurantId;  // Use ID instead of full Restaurant object

    @NotNull(message = "Table number cannot be null")
    private Integer tableNumber;

    @NotBlank(message = "Section cannot be blank")
    @Size(max = 50, message = "Section cannot exceed 50 characters")
    private String section;  // e.g., "Main Dining", "Patio", "Bar Area"

    @NotNull(message = "Capacity cannot be null")
    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;

    @NotNull(message = "Availability status cannot be null")
    private Boolean isAvailable;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public RestaurantTableDTO() {
    }

    /**
     * Constructor with all fields (for creating from entity)
     */
    public RestaurantTableDTO(Long id, Long restaurantId, Integer tableNumber,
                              String section, Integer capacity, Boolean isAvailable,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.tableNumber = tableNumber;
        this.section = section;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructor for creating new tables (without ID, timestamps)
     */
    public RestaurantTableDTO(Long restaurantId, Integer tableNumber,
                              String section, Integer capacity) {
        this.restaurantId = restaurantId;
        this.tableNumber = tableNumber;
        this.section = section;
        this.capacity = capacity;
        this.isAvailable = true;  // Default to available
    }

    // ==================== GETTERS & SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ==================== HELPER METHODS ====================

    /**
     * Get a human-readable description of the table (mirrors entity method)
     */
    public String getDescription() {
        return String.format("Table %d - %s (Capacity: %d)",
                this.tableNumber, this.section, this.capacity);
    }

    @Override
    public String toString() {
        return "RestaurantTableDTO{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", tableNumber=" + tableNumber +
                ", section='" + section + '\'' +
                ", capacity=" + capacity +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

