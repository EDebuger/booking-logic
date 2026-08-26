package com.EDebugger.booking_logic.dtos;
import com.EDebugger.booking_logic.enums.BookingStatus;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.EDebugger.booking_logic.models.User;
import org.jspecify.annotations.NonNull;

public class BookingUserDto { // every booking, current and past can be kept in one table
    // have dedicated views for each
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NonNull
    private User userId;

    @NonNull
    private String restaurantName;

    @NonNull
    private int tableNum;

    private int partySize;

    private LocalDate dateMade;

    private LocalDate bookingDate;

    @NonNull
    @Enumerated
    private BookingStatus status;

    @NonNull
    private LocalDateTime updatedAt;


    public BookingUserDto() {
    }

    public BookingUserDto(Long id, @NonNull User userId, @NonNull String restaurantName, @NonNull int tableNum, int partySize, LocalDate dateMade, LocalDate bookingDate, @NonNull BookingStatus status, @NonNull LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.tableNum = tableNum;
        this.partySize = partySize;
        this.dateMade = dateMade;
        this.bookingDate = bookingDate;
        this.status = status;
        this.updatedAt = updatedAt;
    }


    public Long getId() {
        return id;
    }


    public @NonNull User getUserId() {
        return userId;
    }


    public @NonNull String getRestaurantName() {
        return restaurantName;
    }


    public @NonNull int getTableNum() {
        return tableNum;
    }


    public int getPartySize() {
        return partySize;
    }


    public LocalDate getDateMade() {
        return dateMade;
    }


    public LocalDate getBookingDate() {
        return bookingDate;
    }


    public BookingStatus getStatus() {
        return status;
    }


    public @NonNull LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

