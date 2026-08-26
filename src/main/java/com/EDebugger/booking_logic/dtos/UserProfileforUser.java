package com.EDebugger.booking_logic.dtos;

import com.EDebugger.booking_logic.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;

import org.jspecify.annotations.NonNull;

public class UserProfileforUser {

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

    public UserProfileforUser() {
    }


    public UserProfileforUser(Long id, @NonNull String userName, @NonNull String email, @NonNull String phone, @NonNull Role userRole, LocalDate memberSince, LocalDate updatedAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
        this.memberSince = memberSince;
        this.updatedAt = updatedAt;
    }

    public @NonNull String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName; // make it changable?
    }

    public Long getId() {
        return id;
    }

    public @NonNull String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone; // phone wil be optional
    }

    public @NonNull String getEmail() {
        return email;
    }

    public @NonNull Role getUserRole() {
        return userRole;
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
