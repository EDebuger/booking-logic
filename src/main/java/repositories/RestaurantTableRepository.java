package repositories;

import models.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {


     // Find all tables for a specific restaurant.
    List<RestaurantTable> findByRestaurantIdOrderByTableNumber(Long restaurantId);


      // Find available tables for a specific restaurant.
    @Query("""
        SELECT rt FROM RestaurantTable rt
        WHERE rt.restaurant.id = :restaurantId
        AND rt.isAvailable = true
        ORDER BY rt.tableNumber ASC
    """)
    List<RestaurantTable> findAvailableTablesByRestaurant(@Param("restaurantId") Long restaurantId);

     // Find tables by section within a restaurant.
    @Query("""
        SELECT rt FROM RestaurantTable rt
        WHERE rt.restaurant.id = :restaurantId
        AND rt.section = :section
        ORDER BY rt.tableNumber ASC
    """)
    List<RestaurantTable> findTablesByRestaurantAndSection(
            @Param("restaurantId") Long restaurantId,
            @Param("section") String section
    );

    /**
     * Find available tables with minimum capacity.
     */
    @Query("""
        SELECT rt FROM RestaurantTable rt
        WHERE rt.restaurant.id = :restaurantId
        AND rt.isAvailable = true
        AND rt.capacity >= :minCapacity
        ORDER BY rt.capacity ASC
    """)
    List<RestaurantTable> findAvailableTablesByCapacity(
            @Param("restaurantId") Long restaurantId,
            @Param("minCapacity") Integer minCapacity
    );

    /**
     * Count available tables in a restaurant.
     */
    @Query("""
        SELECT COUNT(rt) FROM RestaurantTable rt
        WHERE rt.restaurant.id = :restaurantId
        AND rt.isAvailable = true
    """)
    long countAvailableTablesByRestaurant(@Param("restaurantId") Long restaurantId);

    /**
     * Find all sections in a restaurant.
     */
    @Query("""
        SELECT DISTINCT rt.section FROM RestaurantTable rt
        WHERE rt.restaurant.id = :restaurantId
        ORDER BY rt.section ASC
    """)
    List<String> findDistinctSectionsByRestaurant(@Param("restaurantId") Long restaurantId);
}
