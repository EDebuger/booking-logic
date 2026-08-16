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

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required, seriously")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password; // Stored as bcrypt hash

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

    public CreateUserDto(String userName, String email, String phone, String password, @NonNull Role userRole, LocalDate memberSince, LocalDate updatedAt) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userRole = userRole;
        this.memberSince = memberSince;
        this.updatedAt = updatedAt;
    }


    public @FutureOrPresent LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@FutureOrPresent LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public @PastOrPresent(message = "Don't live in the future, pal") LocalDate getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(@PastOrPresent(message = "Don't live in the future, pal") LocalDate memberSince) {
        this.memberSince = memberSince;
    }

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
