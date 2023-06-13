package med.voll.api.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.TokenExpiredException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice   
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handle404Error() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TokenExpiredException.class) 
    public ResponseEntity<Object> handleExpiredToken() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle400Error(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(BadRequestErrorBody::new).toList());
    }

    private record BadRequestErrorBody(String field, String message) {
        public BadRequestErrorBody(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
