package com.EDebugger.booking_logic.dtos;

import com.EDebugger.booking_logic.enums.ServiceType;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;

// model will be shown like this in search on the website
public class RestaurantPresentationForUser {

    private Long id;


    private String name;

    private String adress;

    private String postalCode;

    private ServiceType serviceType;

    private String description;

    private Long priceRange;

    private BigDecimal rating; // 0.1-5.0

    private String image; // it's an image link

    private int subOf;


    public RestaurantPresentationForUser() {
    }

    public RestaurantPresentationForUser(Long id, String name, String adress, String postalCode, ServiceType serviceType, String description, Long priceRange, BigDecimal rating, String image, int subOf) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.postalCode = postalCode;
        this.serviceType = serviceType;
        this.description = description;
        this.priceRange = priceRange;
        this.rating = rating;
        this.image = image;
        this.subOf = subOf; //subsidary of a company
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

    public @Nullable BigDecimal getRating() {return rating;}

    public @Nullable String getImage() {return image;}

    public @NonNull int getSubOf() {
        return subOf;
    }
}
