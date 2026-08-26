package com.EDebugger.booking_logic.repositories;

import com.EDebugger.booking_logic.dtos.CreateUserDto;
import com.EDebugger.booking_logic.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.EDebugger.booking_logic.models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    //@Query(value = "SELECT DISTINCT FROM User WHERE user_name=:name", nativeQuery = true)
    //Optional<User> findByUserName(@Param(value = "name") @NonNull String name);// returns user
    User findByUserName(@NonNull String name);// returns user

    User findByEmail(@NonNull String email); // returns user

    Boolean existsByUserName(@NonNull String name); // confirms they exist

    List<User> findByUserRole(@NonNull Role userRole);

    User save(CreateUserDto createUserDto);

    @Query(value = """
        SELECT * FROM User
        WHERE member_since >= :date
        ORDER BY member_since DESC
    """, nativeQuery = true)
    List<User> findRecentUsers(@Param("date") java.time.LocalDate date);

    boolean existsByEmail(@NotBlank @Email(message = "has to be a proper email") String email);

    boolean existsUserByIdAndPassword(@NonNull Long id, @NonNull String pass);

    @Modifying
    @Query(value = "UPDATE users SET phone=:phone WHERE id=:id", nativeQuery = true)
    User setNewPhone(@Param(value = "id") Long id, @Param(value = "phone") String phone);

    @Modifying
    @Query(value = "UPDATE users SET user_name=:name WHERE id=:id", nativeQuery = true)
    User setNewUserName(@Param(value = "id") Long id, @Param(value = "name") String name);

    @Modifying
    @Query(value = "UPDATE users SET password=:password WHERE id=:id", nativeQuery = true)
    User setNewPassword(@Param(value = "id") Long id, @Param(value = "password") String password);

    User findByUserNameOrEmail(String name, String email);
}
