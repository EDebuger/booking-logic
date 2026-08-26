package com.EDebugger.booking_logic.dtos;

import com.EDebugger.booking_logic.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.NonNull;

import java.sql.Date;
import java.time.LocalDate;

public class CreateUserDto {


    @NotBlank(message = "You need to fill out a name")
    @Size(min = 3,max = 20,message = "Don't exceed limit")
    private String userName;

    @NotBlank
    @Email(message = "has to be a proper email")
    private String email;

    @NotBlank(message = "Write your number please")
    @Size(min = 8,max = 8, message = "8 characters limit")
    private String phone;

    @NotBlank(message = "Password is required, seriously")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password; // Stored as bcrypt hash

    @Enumerated(EnumType.STRING)
    @NonNull
    @Valid
    private Role userRole;

    @CreationTimestamp
    private Date member_since;


    public CreateUserDto() {
    }

    public CreateUserDto(String userName, String email, String phone, String password, @NonNull Role userRole, Date member_since) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userRole = userRole;
        this.member_since = member_since;
    }

    public Date getMember_since() {return member_since;}

    public void setMember_since(Date member_since) {this.member_since = member_since;}

    public @NonNull @Valid Role getUserRole() {
        return userRole;
    }

    public void setUserRole(@NonNull @Valid Role userRole) {
        this.userRole = userRole;
    }

    public @NotBlank(message = "Password is required, seriously") @Size(min = 8, message = "Password must be at least 8 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required, seriously") @Size(min = 8, message = "Password must be at least 8 characters") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Write your number please") @Size(min = 8, max = 8, message = "8 characters limit") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "Write your number please") @Size(min = 8, max = 8, message = "8 characters limit") String phone) {
        this.phone = phone;
    }

    public @NotBlank @Email(message = "has to be a proper email") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email(message = "has to be a proper email") String email) {
        this.email = email;
    }

    public @NotBlank(message = "You need to fill out a name") @Size(min = 3, max = 20, message = "Don't exceed limit") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "You need to fill out a name") @Size(min = 3, max = 20, message = "Don't exceed limit") String userName) {
        this.userName = userName;
    }
}
