package ec.com.banco.exception;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}

    // Manejo de recursos no encontrados
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse err = new ErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // Validaciones de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    // Para IllegalArgumentException y otras reglas de negocio
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegal(IllegalArgumentException ex) {

        ErrorResponse err = new ErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de negocio",
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(err);
    }

    // Fallback general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {

        ErrorResponse err = new ErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno",
                ex.getMessage(),
                null
        );
        return ResponseEntity.internalServerError().body(err);
    }
}
