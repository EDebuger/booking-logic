package models;

import enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

import static enums.Role.*;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_name", columnList = "user_name"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_user_role", columnList = "user_role")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    @NonNull
    private String userName;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "phone")
    @NonNull
    private String phone;

    @Column(name = "password", nullable = false)
    private String password; // Stored as bcrypt hash

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @NonNull
    private Role userRole;

    @Column(name = "member_since")
    private LocalDate memberSince;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    public User() {
    }

    public User(Long id, @NonNull String userName, @NonNull String email, @NonNull String phone, String password, @NonNull Role userRole, LocalDate memberSince, LocalDate updatedAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public @NonNull String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isPresent() {
        return false;
    }
}
