package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeUserDto {
    // For individual validations
    public class ChangePhoneValidationDto {
        @NotBlank
        @Size(min = 8, max = 8, message = "Number must be exactly 8 digits")
        private String phone;
        private String currentPassword;
        // getters/setters

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public String getCurrentPassword() {
            return currentPassword;
        }
    }

    public class ChangeUsernameValidationDto {
        @NotBlank
        @Size(min = 3, max = 20, message = "At least 3 and no more than 20 letters")
        private String newUsername;
        private String currentPassword;
        // getters/setters

        public String getNewUsername() {
            return newUsername;
        }

        public void setNewUsername(String newUsername) {
            this.newUsername = newUsername;
        }

        public String getCurrentPassword() {
            return currentPassword;
        }
    }

    public class ChangePasswordValidationDto {
        @NotBlank
        @Size(min = 8, message = "make it at least 8 characters, bud")
        private String currentPassword;
        private String newPassword;
        // getters/setters

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }

    // For applying changes (shared endpoint)
    public class ApplyProfileChangesDto {
        @NotBlank
        @Size(min = 8, max = 8, message = "Number must be exactly 8 digits")
        private String phone;
        @NotBlank
        @Size(min = 3, max = 20, message = "At least 3 and no more than 20 letters")
        private String username;
        @NotBlank
        @Size(min = 8, message = "make it at least 8 characters, bud")
        private String newPassword;
        // getters/setters (all optional, null = don't change)

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

}
