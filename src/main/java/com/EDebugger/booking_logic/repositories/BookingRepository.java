package com.EDebugger.booking_logic.repositories;

import com.EDebugger.booking_logic.models.Booking;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long>{

    Booking findByUserId(@NonNull Long id);

    Booking findByRestaurantName(@NonNull String name);
}
