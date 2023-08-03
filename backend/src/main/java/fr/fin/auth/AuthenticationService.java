package fr.fin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
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
			Staff staff = (Staff) staffService.loadUserByUsername(request.getUsername());
			
			if(staff.getPasswordTrial() < 1) {
				staff.setPasswordTrial(staff.getPasswordTrial() + 1);
				staffService.saveStaff(staff);
			} 
			throw new BadCredentialsException("Bad credentials");	
		} catch(LockedException e) {
			throw new LockedException("Account is locked");
		}

		// If authentication manager authenticates, it process the JWT token generation
		Staff staff = (Staff) staffService.loadUserByUsername(request.getUsername());
		String generatedJwt = jwtService.generateToken(staff);
		return new JwtTokenResponse(generatedJwt);
	}
}
