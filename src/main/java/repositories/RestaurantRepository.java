package repositories;

import enums.ServiceType;
import models.Restaurant;
import dtos.RestaurantPresentationForUser;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{
    //SELECT * FROM restaurants WHERE name=?
    List<RestaurantPresentationForUser> findByName(String name);

    List<RestaurantPresentationForUser> findByServiceType(@NonNull ServiceType serviceType);

    List<RestaurantPresentationForUser> findRestaurantByPriceRangeWithin(@NonNull int num);

    List<RestaurantPresentationForUser> findRestaurantByPriceRangeBeyond(@NonNull int num);

    List<RestaurantPresentationForUser> findRestaurantBysubOf(@NonNull String com);
}
