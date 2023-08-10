package fr.fin.auth;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Staff;
import fr.fin.service.StaffService;

@Service
public class AuthenticationService {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtTokenResponse authenticate(JwtLoginRequest request)  {

		// Authentication is made with Authentication Manager with username and password
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch(BadCredentialsException e) {
			// Catch BadCredentials to increment passwordTrial
			Staff staff = (Staff) staffService.loadUserByUsername(request.getUsername());
			if(staff != null) {
				staff.setPasswordTrial(staff.getPasswordTrial() + 1);
				staffService.saveStaff(staff);
			}
			throw new BadCredentialsException("Les identifiants sont incorrects");
		}

		// If authentication manager authenticates, it process the JWT token generation and sets the password to 0
		Staff staff = (Staff) staffService.loadUserByUsername(request.getUsername());
		staff.setPasswordTrial(0);
		staffService.saveStaff(staff);

		// Proccess extra claims
		HashMap<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("id", staff.getId());
		extraClaims.put("role", staff.getRole());

		String generatedJwt = jwtService.generateToken(extraClaims, staff);
		return new JwtTokenResponse(generatedJwt);
	}
}
