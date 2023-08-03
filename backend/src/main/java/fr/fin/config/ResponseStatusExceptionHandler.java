package fr.fin.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ResponseStatusExceptionHandler {
    
	@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(ResponseStatusException ex) {
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(ex.getStatusCode().value(), ex.getMessage()), ex.getStatusCode());
    }
}