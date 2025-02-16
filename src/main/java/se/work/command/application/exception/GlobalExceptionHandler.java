package se.work.command.application.exception;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.work.query.exception.NoWorkOrderFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoWorkOrderFoundException.class, AggregateNotFoundException.class})
    public ResponseEntity<String> handleNotFoundScenarios(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequestScenarios(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
