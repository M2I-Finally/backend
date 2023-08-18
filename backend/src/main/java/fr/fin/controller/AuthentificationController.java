package fr.fin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.auth.AuthenticationService;
import fr.fin.auth.JwtLoginRequest;
import fr.fin.auth.JwtTokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Authentification", description = "Endpoint for authentification")
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
			throw new LockedException("Le compte est verrouillé");
		} catch (DisabledException e) {
			throw new DisabledException("Le compte est désactivé");
		}

		return ResponseEntity.ok(tokenResponse);
	}

}
