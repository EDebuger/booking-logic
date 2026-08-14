package dtos;
import enums.BookingStatus;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "bookings")
public class CreateBookingDto { // every booking, current and past can be kept in one table
    // have dedicated views for each

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotBlank(message = "booking needs to belong to someone")
    private User userId;

    @Column(name = "restaurant_name",length = 50,unique = false)
    @NotBlank(message = "which restaurant?")
    private String restaurantName;

    @Column(name = "table_num")
    @NotBlank(message = "has to reference a table")
    @Positive(message = "is the table number right?")
    @Max(value = 24,message = "Number is too high >_>")
    private int tableNum;

    @Column(name = "party_size")
    @NotBlank(message = "Even if you're alone, you have to write it")
    @Positive
    @Max(value = 8,message = "You can't bring more friends, pal")
    private int partySize;

    @Column(name = "date_made")
    @DateTimeFormat
    @PastOrPresent
    private LocalDate dateMade; // gets value when inserted anyway

    @Column(name = "booking_date")
    @NotBlank(message = "Input a date, please")
    @FutureOrPresent(message = "Don't live in the past,pal")
    private LocalDate bookingDate;

    @Column(name = "status")
    @NonNull
    @Enumerated
    @Valid
    private BookingStatus status; // default value 'pending' when inserted

    @Column(name = "updated_at")
    @NonNull
    private LocalDate updatedAt; // triggered when changed


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

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }



}
