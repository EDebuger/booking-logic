package repositories;

import enums.ServiceType;
import models.Restaurant;
import dtos.RestaurantPresentationForUser;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{
    //SELECT * FROM restaurants WHERE name=?
    @Query(value = "SELECT * FROM restaurants WHERE name IS LIKE :name", nativeQuery = true)
    List<Restaurant> findByName(@Param(value = "name") String name);

    List<Restaurant> findByServiceType(@NonNull ServiceType serviceType);

    @Query(value = "SELECT * FROM restaurants WHERE price_range = :num OR price_range < :num", nativeQuery = true)
    List<Restaurant> findRestaurantByPriceRangeWithin(@NonNull int num);

    @Query(value = "SELECT * FROM restaurants WHERE price_range = :num OR price_range > :num", nativeQuery = true)
    List<Restaurant> findRestaurantByPriceRangeBeyond(@NonNull int num);

    @Query(value = "SELECT r FROM restaurants r WHERE r.rating>:p-0.1 AND r.rating<:p+1", nativeQuery = true)
    List<Restaurant> findRestaurantByRating(double p);

    @Query(value = "SELECT * FROM restaurants WHERE sub_of=(SELECT id FROM partner_companies WHERE name=:com)", nativeQuery = true)
    List<Restaurant> findRestaurantBysubOf(@NonNull String com);
}
