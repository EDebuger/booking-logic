package services;

import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import enums.Role;
import models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ResponseBody;
import repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserProfileforAdmin> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserProfileforAdmin convertToAdminDTO(User user) {
        UserProfileforAdmin dto = new UserProfileforAdmin( //dto without setters
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhone(),
                user.getUserRole(),
                user.getMemberSince(),
                user.getUpdatedAt()
        );
        // Map only the fields you need for the admin profile
        return dto;
    }
    private UserProfileforUser convertToUserDTO(User user) {
        UserProfileforUser dto = new UserProfileforUser( //dto without setters
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhone(),
                user.getUserRole(),
                user.getMemberSince(),
                user.getUpdatedAt()
        );
        // Map only the fields you need for the admin profile
        return dto;
    }
}
