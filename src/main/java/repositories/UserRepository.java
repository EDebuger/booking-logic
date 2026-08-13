package repositories;

import dtos.CreateUserDto;
import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import enums.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    User findByUserName(@NonNull String name);

    Boolean existsByUserName(@NonNull String name);

    List<User> findByUserRole(@NonNull Role userRole);

    User save(CreateUserDto createUserDto);

}
