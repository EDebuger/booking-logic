package repositories;

import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import enums.Role;
import models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>{

    User findByUserName(@NonNull String name);

    List<UserProfileforAdmin> findByUserRole(@NonNull Role userRole);
}
