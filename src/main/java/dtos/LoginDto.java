package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public LoginDto() {
    }

    public LoginDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
