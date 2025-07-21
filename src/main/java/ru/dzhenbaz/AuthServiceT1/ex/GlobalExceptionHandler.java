package ru.dzhenbaz.AuthServiceT1.ex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({
            InvalidRefreshTokenException.class,
            UserNotCreatedException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleCustom(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        Map<String, String> error = Map.of("error", "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
