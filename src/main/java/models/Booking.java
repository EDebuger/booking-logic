package models;
import enums.BookingStatus;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "bookings")
public class Booking { // every booking, current and past can be kept in one table
                        // have dedicated views for each
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User userId;

    @Column(name = "restaurant_name",length = 50,unique = false)
    @NonNull
    private String restaurantName;

    @Column(name = "table_num")
    @NonNull
    private int tableNum;

    @Column(name = "party_size")
    private int partySize;

    @Column(name = "date_made")
    private LocalDate dateMade;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "status")
    private BookingStatus status;

    public Booking() {
    }


    public Booking(Long id, @NonNull User userId ,@NonNull String restaurantName, @NonNull int tableNum, int partySize, LocalDate dateMade, LocalDate bookingDate, BookingStatus status) {
        this.id = id;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.tableNum = tableNum;
        this.partySize = partySize;
        this.dateMade = dateMade;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDateMade() {
        return dateMade;
    }

    public void setDateMade(LocalDate dateMade) {
        this.dateMade = dateMade;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
