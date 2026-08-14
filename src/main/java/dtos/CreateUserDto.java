package dtos;

import enums.Role;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Table(name = "users")
public class CreateUserDto {


    @Column(name = "user_name")
    @NotBlank(message = "You need to fill out a name")
    @Size(min = 3,max = 20,message = "Don't exceed limit")
    private String userName;

    @Column(name = "email")
    @NotBlank
    @Email(message = "has to be a proper email")
    private String email;

    @Column(name = "phone")
    @NotBlank(message = "Write your number please")
    @Size(min = 8,max = 8, message = "8 characters limit")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @NonNull
    @Valid
    private Role userRole;

    @Column(name = "member_since")
    @PastOrPresent(message = "Don't live in the future, pal")
    private LocalDate memberSince;

    @Column(name = "updated_at")
    @FutureOrPresent
    private LocalDate updatedAt;


    public CreateUserDto() {
    }


    public CreateUserDto(@NonNull String userName, @NonNull String email, @NonNull String phone, @NonNull Role userRole, LocalDate memberSince, LocalDate updatedAt) {
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
        this.userName = userName;
    }


    public @NonNull String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public @NonNull Role getUserRole() {
        return userRole;
    }

    public void setUserRole(@NonNull Role userRole) {
        this.userRole = userRole;
    }

    public LocalDate getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(LocalDate memberSince) {
        this.memberSince = memberSince;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
