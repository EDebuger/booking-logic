package ExceptionHandlers;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    public class ErrorResponse {
        private int status;
        private String message;


        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {return status;}
        public String getMessage() {return message;}
    }

    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
