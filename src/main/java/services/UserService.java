package services;

import dtos.CreateUserDto;
import dtos.UserProfileforAdmin;
import dtos.UserProfileforUser;
import enums.Role;
import jakarta.transaction.Transactional;
import models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import repositories.UserRepository;
import ExceptionHandlers.GlobalExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static jdk.internal.org.jline.utils.Log.*;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final GlobalExceptionHandler globalExceptionHandler;

    public UserService(UserRepository userRepository, GlobalExceptionHandler globalExceptionHandler) {
        this.userRepository = userRepository;
        this.globalExceptionHandler = globalExceptionHandler;
    }


    public UserProfileforUser getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User "+id+" could not be found"));
        return convertToUserDTO(user);
    }

    public UserProfileforUser getUserByName(@PathVariable String name) {
        User user = userRepository.findByUserName(name);
                if(user == null) throw new GlobalExceptionHandler.ResourceNotFoundException("User "+name+" could not be found");
        return convertToUserDTO(user);
    }

    public List<UserProfileforAdmin> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileforAdmin> getByUserRole() {
        List<User> users = userRepository.findByUserRole(Role.USER);
        return users.stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileforAdmin> getByAdminRole() {
        List<User> users = userRepository.findByUserRole(Role.ADMIN);
        return users.stream()
                .map(this::convertToAdminDTO)
                .collect(Collectors.toList());
    }

    public List<UserProfileforAdmin> getDefunctUsers() { // prfoiles no longer active
        List<User> users = userRepository.findByUserRole(Role.DEFUNCT);
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

    @Transactional
    public User createUser(User user, String rawPassword) {
        info("Creating user: {}", user.getUserName());

        // Check if user already exists
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUserName());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Encode the password
        //String encodedPassword = SecurityConfig.passwordEncoder(rawPassword);
        //user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public @ResponseBody UserProfileforUser createUser(@RequestBody CreateUserDto createUserDto) {
        if (!userRepository.existsByUserName(createUserDto.getUserName())) {
            throw  new RuntimeException("User with name -"+createUserDto.getUserName()+"- already exists");
        }
        else {User user = userRepository.save(createUserDto);
        UserProfileforUser us = convertToUserDTO(user);
        return us;} // retrieve entity | transform to dto
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
