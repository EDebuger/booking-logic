package models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_name")
    @NonNull
    private String restaurantName;

    @Column(name = "table_num")
    @NonNull
    private int tableNum;

    @Column(name = "party_size")
    private int partySize;

    public Booking() {
    }
}
