package repositories;

import enums.Role;
import models.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>{

    User findByUserName(@NonNull String name);

    List<User> findByUserRole(@NonNull Role userRole);
}
