package dtos;

import enums.Role;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.jspecify.annotations.NonNull;

import static enums.Role.*;

@Entity
@Table(name = "users")
public class UserProfileforAdmin {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @NonNull
    private Role userRole;

    @Column(name = "member_since")
    private LocalDate memberSince;

    @Column(name = "created_at")
    private LocalTime createdAt;

    @Column(name = "updated_at")
    private LocalTime updatedAt;

    public UserProfileforAdmin() {
    }


    public UserProfileforAdmin(Long id, @NonNull String userName, @NonNull String email, @NonNull String phone, @NonNull Role userRole, LocalDate memberSince, LocalTime createdAt, LocalTime updatedAt) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
        this.memberSince = memberSince;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public @NonNull String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
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

    public LocalTime getCreatedAt() {
        return createdAt;
    }

    public LocalTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
