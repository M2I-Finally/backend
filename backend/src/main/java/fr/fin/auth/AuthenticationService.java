package fr.fin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import fr.fin.model.dto.AuthenticationResponse;
import fr.fin.model.dto.JwtLoginDto;
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

	public AuthenticationResponse authenticate(JwtLoginDto request) {
		
		// Authentication is made with Authentication Manager with username and password
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		// If authentication manager authenticates, it process the JWT token generation
		Staff staff = (Staff) staffService.loadUserByUsername(request.getUsername());
		String generatedJwt = jwtService.generateToken(staff);
		return new AuthenticationResponse(generatedJwt);
	}
}
