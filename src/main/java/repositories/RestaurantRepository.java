package repositories;

import enums.ServiceType;
import models.Restaurant;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{
    //SELECT * FROM restaurants WHERE name=?
    Restaurant findByName(String name);

    Restaurant findByServiceType(@NonNull ServiceType serviceType);
}
