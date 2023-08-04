package fr.fin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import fr.fin.exceptions.custom.ActionForbiddenException;
import fr.fin.exceptions.custom.ResourceAlreadyExistsException;
import fr.fin.exceptions.custom.ResourceNotFoundException;
import fr.fin.exceptions.custom.ValidationErrorException;

@RestControllerAdvice
@CrossOrigin
public class ControllerExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ResponseError handleResourceNotFoundException(Exception ex, WebRequest request) {
		return new ResponseError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseError handleResourceAlreadyExistsException(Exception ex, WebRequest request) {
		return new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
	
	@ExceptionHandler(ActionForbiddenException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ResponseError handleActionForbiddenException(Exception ex, WebRequest request) {
		return new ResponseError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
	}
	
	@ExceptionHandler(ValidationErrorException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseError handleValidationErrorException(Exception ex, WebRequest request) {
		return new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}
}