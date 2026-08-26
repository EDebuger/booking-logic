package com.EDebugger.booking_logic.dtos;

import com.EDebugger.booking_logic.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;

import org.jspecify.annotations.NonNull;

public class UserProfileforAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String userName;

    @NonNull
    private String email;

    @NonNull
    private String phone;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Role userRole;

    private LocalDate memberSince;

    private LocalDate updatedAt;


    public UserProfileforAdmin(Long id, @NonNull String userName, @NonNull String email, @NonNull String phone, @NonNull Role userRole, LocalDate memberSince, LocalDate updatedAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
        this.memberSince = memberSince;
        this.updatedAt = updatedAt;
    }

    public UserProfileforAdmin() {

    }

    public Long getId() {
        return id;
    }
    public @NonNull String getUserName() {
        return userName;
    }

    public @NonNull String getPhone() {
        return phone;
    }

    public @NonNull String getEmail() {
        return email;
    }


    public @NonNull Role getUserRole() {
        return userRole;
    }

    public void setUserRole(@NonNull Role userRole) {
        this.userRole = userRole; // admin can change you
    }

    public LocalDate getMemberSince() {
        return memberSince;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
