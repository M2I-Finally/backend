package fr.fin.controller;

/**
 * PasswordValidator, use for validating user's password.
 * Objectify the class and verify isValid.
 * Advantage is to get precise message when password is not valid.
 */
public class PasswordValidator {

	private String password;
	private String message;
	
	
	public PasswordValidator() {
	}

	public PasswordValidator(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isValid(String password) {
		
		// password should be longer than 8		
		if (password.length() < 8) {
			setMessage( "Le password doit avoir minimume 8 characters.");
			return false;
		}
		
		// password should have at least one uppercase alphabet
		String upperCaseChars = "(.*[A-Z].*)";
		if (!password.matches(upperCaseChars)) {
			setMessage("Password doit contenir au moins une lettre majuscule.");
			return false;
		}
		
		// password should have at least one lowercase alphabet
		String lowerCaseChars = "(.*[a-z].*)";
		if (!password.matches(lowerCaseChars)) {
			setMessage( "Password doit contenir au moins une lettre minuscule.");
			return false;
		}
		
		//password should contain at least one number
		String numbers = "(.*[0-9].*)";
		if (!password.matches(numbers)) {
			setMessage( "Password doit contenir au moins un chiffre.");
			return false;
		}

		//password should contain at least one special character
		String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
		if (!password.matches(specialChars)) {
			setMessage( "Password doit contenir au moins un character spécial.");
			return false;
		}
		return true;
	}
}
