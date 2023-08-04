package fr.fin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import fr.fin.exceptions.custom.ResourceNotFoundException;

@RestControllerAdvice
@CrossOrigin
public class ControllerExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseError handleResourceNotFoundExceptionn(Exception ex, WebRequest request) {
		return new ResponseError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
	}
}