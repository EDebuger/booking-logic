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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void deleteUserById(Long id, Role role) {
        if (userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        if (role!=Role.USER) { // this delete function is only meant for users
            throw new RuntimeException("User with incorrect role");
        }
        userRepository.deleteById(id);
    }
    public void deleteAdminById(Long id, Role role) {
        if (userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        if (role!=Role.ADMIN) { // this delete function is only meant for users
            throw new RuntimeException("User with incorrect role");
        }
        userRepository.deleteById(id);
    }

//    @Transactional
//    public User createUser(@RequestBody CreateUserDto user) {
//        info("Creating user: {}", user.getUserName());
//
//        // Check if user already exists
//        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
//            throw new IllegalArgumentException("Username already exists: " + user.getUserName());
//        }
//
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
//        }
//
//        return userRepository.save(user);
//    }

    @Transactional
    public @ResponseBody UserProfileforUser createUser(@RequestBody CreateUserDto createUserDto) {
        if (userRepository.existsByUserName(createUserDto.getUserName())) {
            throw  new RuntimeException("User with name -"+createUserDto.getUserName()+"- already exists");
        }
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw  new RuntimeException("User with email -"+createUserDto.getEmail()+"- already exists");
        }
        else {User user = userRepository.save(createUserDto);
        UserProfileforUser us = convertToUserDTO(user);
        return us;} // retrieve entity | transform to dto
    }


    public Map<String, Object> validatePhoneChange(Long userId, String phone, String currentPassword) {
        if(userRepository.existsById(userId)) {
            if(userRepository.existsUserByIdAndPassword(userId,currentPassword)) {
                return Map.of(
                        "success", true,
                        "message", "Phone number is valid and ready to update to"+phone
                );

            } else {throw new RuntimeException("Wrong password for user:"+userId);}
        } else {throw new RuntimeException("User with id:"+userId+" does not exist");}
    }

    public Map<String, Object> validateUsernameChange(Long userId, String newUsername, String currentPassword) {
        if(userRepository.existsById(userId)) {
            if(userRepository.existsUserByIdAndPassword(userId,currentPassword)) {
                return Map.of(
                        "success", true,
                        "message", "Username is valid and ready to update to"+newUsername
                );

            } else {throw new RuntimeException("Wrong password for user:"+userId);}
        } else {throw new RuntimeException("User with id:"+userId+" does not exist");}
    }

    public Map<String, Object> validatePasswordChange(Long userId, String currentPassword, String newPassword) {
        if(userRepository.existsById(userId)) {
            if(userRepository.existsUserByIdAndPassword(userId,currentPassword)) {
                return Map.of(
                        "success", true,
                        "message", "Password is valid and ready to update"
                );

            } else {throw new RuntimeException("Wrong password for user:"+userId);}
        } else {throw new RuntimeException("User with id:"+userId+" does not exist");}
    }

    @Transactional
    public Map<String, Object> applyProfileChanges(Long userId, String phone, String username, String newPassword) {
        if(userRepository.existsById(userId)) { // set them all
            userRepository.setNewPhone(userId,phone);
            userRepository.setNewUserName(userId,username);
            userRepository.setNewPassword(userId,newPassword);

            return Map.of(
                    "success", true,
                    "message", "Profile updated successfully",
                    "updatedFields", Map.of(
                            "new phone", phone,
                            "username", username
                    )   );
        } else {throw new RuntimeException(
                "User with id:"+userId+" does not exist");
        }
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
