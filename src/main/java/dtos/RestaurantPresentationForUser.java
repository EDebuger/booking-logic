package dtos;

import enums.ServiceType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "restaurants")    // model will be shown like this in search on the website
public class RestaurantPresentationForUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "adress",length = 50,unique = true)
    @NonNull
    private String adress;

    @Column(name = "postal_code",length = 5,unique = true)
    @NonNull
    private String postalCode;

    @Column(name = "service_type")
    @NonNull
    @Enumerated
    private ServiceType serviceType;

    @Column(name = "description",length = 150,unique = true)
    @NonNull
    private String description;

    @Column(name = "price_range") //useful for filtering
    @NonNull
    private Long priceRange;

    @Column(name = "sub_of")
    @NonNull
    private int subOf;


    public RestaurantPresentationForUser() {
    }


    public RestaurantPresentationForUser(Long id, @NonNull String name, @NonNull String adress, @NonNull String postalCode, @NonNull ServiceType serviceType, @NonNull String description, @NonNull Long priceRange, @NonNull int subOf) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.postalCode = postalCode;
        this.serviceType = serviceType;
        this.description = description;
        this.priceRange = priceRange;
        this.subOf = subOf; //subsidary of company
    }
        // will only get info, not change anything
    public @NonNull ServiceType getServiceType() {
        return serviceType;
    }

    public Long getId() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull String getAdress() {
        return adress;
    }

    public @NonNull String getPostalCode() {
        return postalCode;
    }

    public @NonNull String getDescription() {
        return description;
    }

    public @NonNull Long getPriceRange() {
        return priceRange;
    }

    public @NonNull int getSubOf() {
        return subOf;
    }
}
