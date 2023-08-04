package fr.fin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.exceptions.custom.ValidationErrorException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class AuthentificationController {

	@Autowired
	private AuthenticationService authenticationService;

	/**
	 * Log the user with a JWT Token if the authentication service returns a successful response
	 * @param jwtLoginRequest		The Dto used for login
	 */
	@PostMapping("/login")
	public ResponseEntity<JwtTokenResponse> jwtLogin(@RequestBody JwtLoginRequest jwtLoginRequest, BindingResult bindingResult, HttpServletRequest request) throws Exception {
		JwtTokenResponse tokenResponse;
		
		/* ResponseStatusException can't be thrown if there was an exception because the body won't be readable by the client 
		 * Http status is set primarily for the frontend to process the different cases
		 * */
		try {
			tokenResponse = authenticationService.authenticate(jwtLoginRequest);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Les identifiants sont incorrects");
		} catch (LockedException e) {
			throw new BadCredentialsException("Le compte est verouillé");
		}
		
		return ResponseEntity.ok(tokenResponse);
	}

	/**
	 * Generate BCrypt password utility route (will be disabled later)
	 * @param secret	The string to encode using BCrypt
	 * @return			The encoded string
	 * @throws ValidationErrorException 
	 */
	@GetMapping("/bcrypt")
	public String bcryptPasswordGenerator(@RequestParam(value = "secret", required = true) String secret) throws ValidationErrorException {
		
		if(secret == null) {
			throw new ValidationErrorException("Il manque le paramètre 'secret'");
		}
		
		return new BCryptPasswordEncoder().encode(secret);
	}
}
