package com.EDebugger.booking_logic.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_tables", indexes = {
        @Index(name = "idx_restaurant_id", columnList = "restaurant_id"),
        @Index(name = "idx_is_available", columnList = "is_available"),
        @Index(name = "idx_section", columnList = "section")
})
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "table_number", nullable = false)
    @NotNull
    private Integer tableNumber;

    @Column(name = "section", nullable = false, length = 50)
    @NotBlank
    private String section; // e.g., "Main Dining", "Patio", "Bar Area"

    @Column(name = "capacity", nullable = false)
    @NotNull
    private Integer capacity;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable; // db logic defaults it to true

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // keeps check of when table got created

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    /**
     * Automatically set timestamps before persistence.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isAvailable == null) {
            this.isAvailable = true;
        }
    }

    /**
     * Automatically update the updated_at timestamp before update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Convenience method to mark table as available.
     */
    public void markAvailable() {
        this.isAvailable = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Convenience method to mark table as unavailable.
     */
    public void markUnavailable() {
        this.isAvailable = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Get a human-readable description of the table.
     */
    public String getDescription() {
        return String.format("Table %d - %s (Capacity: %d)",
                this.tableNumber, this.section, this.capacity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(@NotNull Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public @NotNull Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(@NotNull Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public @NotBlank String getSection() {
        return section;
    }

    public void setSection(@NotBlank String section) {
        this.section = section;
    }

    public @NotNull Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(@NotNull Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
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

}

