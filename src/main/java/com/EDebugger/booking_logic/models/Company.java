package com.EDebugger.booking_logic.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

@Entity
@Table(name = "partner_companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk will be referenced by restaurants 1/M

    @Column(name = "name",length = 50,unique = true)
    @NonNull
    private String name;

    @Column(name = "telephone",length = 20,unique = true)
    @NonNull
    private String telephone;

    @Column(name = "email",length = 75,unique = true)
    @NonNull
    private String email;

    public Company() {
    }


    public Company(Long id, @NonNull String name, @NonNull String telephone, @NonNull String email) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NonNull String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public @NonNull String getTelephone() {
        return telephone;
    }

    public void setTelephone(@NonNull String telephone) {
        this.telephone = telephone;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}
