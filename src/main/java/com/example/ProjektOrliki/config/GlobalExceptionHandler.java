    package com.example.ProjektOrliki.config;

    import com.example.ProjektOrliki.auth.exception.UserAlreadyExistsException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;
    import org.springframework.http.converter.HttpMessageNotReadableException;

    import java.util.Objects;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
            String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<?> handleHttpMessageNotReadableException() {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Niepoprawny format danych wejściowych");
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleUnexpectedException() {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił nieoczekiwany błąd serwera");
        }

    }
