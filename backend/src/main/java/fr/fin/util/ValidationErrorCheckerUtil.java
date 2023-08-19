package fr.fin.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import fr.fin.exceptions.custom.ValidationErrorException;

public class ValidationErrorCheckerUtil {


	public static void hasValidationErrors(BindingResult bindingResult) throws ValidationErrorException {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<>();

	        for (FieldError error : bindingResult.getFieldErrors()) {
	            errorMessages.add(error.getDefaultMessage());
	        }

	        String errorMessage = String.join(", ", errorMessages);
	        throw new ValidationErrorException("Erreurs de validation : " + errorMessage);
		}
	}

}
