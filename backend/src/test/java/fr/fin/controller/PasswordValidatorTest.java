package fr.fin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {

	@Test
	void when_passwordLessThan8_then_isValidReturnFalse() {
		PasswordValidator passwordValidator = new PasswordValidator();
		assertFalse(passwordValidator.isValid("1@Psw"));
	}
	
	@Test
	void when_passwordLessThan8_then_messageDisplay() {
		PasswordValidator passwordValidator = new PasswordValidator();
		passwordValidator.isValid("1@Psw");
		assertEquals("Le password doit avoir minimume 8 characters.",passwordValidator.getMessage());
	}
	
	@Test
	void when_passwordHasNoUppercase_then_isValidReturnFalse() {
		PasswordValidator passwordValidator = new PasswordValidator();
		assertFalse(passwordValidator.isValid("123passwor@"));
	}
	
	@Test
	void when_passwordHasNoUppercase_then_messageDisplay() {
		PasswordValidator passwordValidator = new PasswordValidator();
		passwordValidator.isValid("123passwor@");
		assertEquals("Password doit contenir au moins une lettre majuscule.",passwordValidator.getMessage());
	}
	
	@Test
	void when_passwordHasNoLowercase_then_isValidReturnFalse() {
		PasswordValidator passwordValidator = new PasswordValidator();
		assertFalse(passwordValidator.isValid("123PASSWOR@"));
	}
	@Test
	void when_passwordHasNoLowercase_then_messageDisplay() {
		PasswordValidator passwordValidator = new PasswordValidator();
		passwordValidator.isValid("123PASSWOR@");
		assertEquals("Password doit contenir au moins une lettre minuscule.",passwordValidator.getMessage());
	}
	@Test
	void when_passwordHasNoNumbers_then_isValidReturnFalse() {
		PasswordValidator passwordValidator = new PasswordValidator();
		assertFalse(passwordValidator.isValid("LongPASSWORD@"));
	}
	
	@Test
	void when_passwordHasNoNumbers_then_messageDisplay() {
		PasswordValidator passwordValidator = new PasswordValidator();
		passwordValidator.isValid("LongPASSWORD@");
		assertEquals("Password doit contenir au moins un chiffre.",passwordValidator.getMessage());
	}
	@Test
	void when_passwordHasNoSpecialCharacters_then_isValidReturnFalse() {
		PasswordValidator passwordValidator = new PasswordValidator();
		assertFalse(passwordValidator.isValid("123PASSword"));
	}
	
	@Test
	void when_passwordHasNoSpecialCharacters_then_messageDisplay() {
		PasswordValidator passwordValidator = new PasswordValidator();
		passwordValidator.isValid("123PASSword");
		assertEquals("Password doit contenir au moins un character spécial.",passwordValidator.getMessage());
	}
	
	@Test
	void when_passwordLengthGT8HasSpecialCharactersLowercaseAndUpperCase_then_isValidReturnTrue() {
		PasswordValidator passwordValidator = new PasswordValidator();
		assertTrue(passwordValidator.isValid("123PASSword@"));
	}
}
