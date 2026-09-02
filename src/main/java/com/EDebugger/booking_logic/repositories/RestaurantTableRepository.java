package com.EDebugger.booking_logic.repositories;

import com.EDebugger.booking_logic.models.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {


     // Find all tables for a specific restaurant.
    List<RestaurantTable> findByRestaurantIdOrderByTableNumber(Long restaurantId);


      // Find available tables for a specific restaurant.
    @Query(value = """
        SELECT * FROM RestaurantTable
        WHERE restaurant_id = :restaurantId
        AND is_available = true
        ORDER BY rt.tableNumber ASC
    """, nativeQuery = true)
    List<RestaurantTable> findAvailableTablesByRestaurant(@Param("restaurantId") Long restaurantId);

     // Find tables by section within a restaurant.
    @Query(value = """
        SELECT * FROM RestaurantTable
        WHERE restaurant_id = :restaurantId
        AND section = :section
        ORDER BY table_number ASC
    """, nativeQuery = true)
    List<RestaurantTable> findTablesByRestaurantAndSection(
            @Param("restaurantId") Long restaurantId,
            @Param("section") String section
    );

    /**
     * Find available tables with minimum capacity.
     */
    @Query(value = """
        SELECT * FROM RestaurantTable
        WHERE restaurant_id = :restaurantId
        AND is_available = 
                (SELECT 1 FROM bookings WHERE booking_date=:date AND restaurant_name=
                                                                         (SELECT 1 FROM restaurants WHERE name=:restaurantId))
        AND capacity >= :minCapacity
        ORDER BY capacity ASC
    """, nativeQuery = true)
    List<RestaurantTable> findAvailableTablesByCapacity(
            @Param("restaurantId") Long restaurantId,
            @Param("minCapacity") Integer minCapacity,
            @Param("date") Date date
    );

    /**
     * Count available tables in a restaurant.
     */
    @Query(value = """
        SELECT COUNT(*) FROM RestaurantTable
        WHERE restaurant_id = :restaurantId
        AND is_available = true
    """, nativeQuery = true)
    long countAvailableTablesByRestaurant(@Param("restaurantId") Long restaurantId);

    /**
     * Find all sections in a restaurant.
     */
    @Query(value = """
        SELECT DISTINCT section FROM RestaurantTable
        WHERE restaurant_id = :restaurantId
        ORDER BY section ASC
    """, nativeQuery = true)
    List<String> findDistinctSectionsByRestaurant(@Param("restaurantId") Long restaurantId);
}
