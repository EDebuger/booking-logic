package com.EDebugger.booking_logic.dtos;
import com.EDebugger.booking_logic.enums.BookingStatus;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import com.EDebugger.booking_logic.models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

public class CreateBookingDto { // every booking, current and past can be kept in one table
    // have dedicated views for each

    @ManyToOne
    @NotBlank(message = "booking needs to belong to someone")
    private User userId;

    @NotBlank(message = "which restaurant?")
    private String restaurantName;

    @NotBlank(message = "has to reference a table")
    @Positive(message = "is the table number right?")
    @Max(value = 24,message = "Number is too high >_>")
    private int tableNum;

    @NotBlank(message = "Even if you're alone, you have to write it")
    @Positive
    @Max(value = 8,message = "You can't bring more friends, pal")
    private int partySize;

    @DateTimeFormat
    @PastOrPresent
    private LocalDate dateMade; // gets value when inserted anyway

    @NotBlank(message = "Input a date, please")
    @FutureOrPresent(message = "Don't live in the past,pal")
    private LocalDate bookingDate;

    @NonNull
    @Enumerated
    @Valid
    private BookingStatus status; // default value 'pending' when inserted

    @NonNull
    private LocalDateTime updatedAt; // triggered when changed


    public CreateBookingDto() {
    }

    public CreateBookingDto(@NonNull User userId, @NonNull String restaurantName, @NonNull int tableNum, int partySize, LocalDate bookingDate) {
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.tableNum = tableNum;
        this.partySize = partySize;
        this.bookingDate = bookingDate;
    }


    public @NonNull User getUserId() {
        return userId;
    }

    public void setUserId(@NonNull User userId) {
        this.userId = userId;
    }

    public @NonNull String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(@NonNull String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public @NonNull int getTableNum() {
        return tableNum;
    }

    public void setTableNum(@NonNull int tableNum) {
        this.tableNum = tableNum;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {this.partySize = partySize;}

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }



}
