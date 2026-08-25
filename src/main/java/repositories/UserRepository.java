package repositories;

import dtos.CreateUserDto;
import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import enums.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    @Query(value = "SELECT DISTINCT FROM User WHERE userName=:name", nativeQuery = true)
    Optional<User> findByUsername(@Param(value = "name") @NonNull String name);// returns user
    User findByUserName(@NonNull String name);// returns user

    User findByEmail(@NonNull String email); // returns user

    Boolean existsByUserName(@NonNull String name); // confirms they exist

    List<User> findByUserRole(@NonNull Role userRole);

    User save(CreateUserDto createUserDto);

    @Query("""
        SELECT u FROM User u
        WHERE u.memberSince >= :date
        ORDER BY u.memberSince DESC
    """)
    List<User> findRecentUsers(@Param("date") java.time.LocalDate date);

    boolean existsByEmail(@NotBlank @Email(message = "has to be a proper email") String email);

    boolean existsUserByIdAndPassword(@NonNull Long id, @NonNull String pass);

    @Query(value = "UPDATE users SET phone=:phone WHERE id=:id", nativeQuery = true)
    User setNewPhone(@Param(value = "id") Long id, @Param(value = "phone") String phone);

    @Query(value = "UPDATE users SET user_name=:name WHERE id=:id", nativeQuery = true)
    User setNewUserName(@Param(value = "id") Long id, @Param(value = "name") String name);

    @Query(value = "UPDATE users SET password=:password WHERE id=:id", nativeQuery = true)
    User setNewPassword(@Param(value = "id") Long id, @Param(value = "password") String password);

    User findByUsernameOrEmail(String param);
}
