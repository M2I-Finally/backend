package fr.fin.auth;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.model.dto.CrsfTokenDto;
import fr.fin.model.dto.LoginDto;
import fr.fin.model.entity.Staff;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthentificationController {

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationService authenticationService;


	@PostMapping("/login")
	public ResponseEntity<JwtTokenResponse> jwtLogin(@RequestBody JwtLoginRequest jwtLoginRequest, BindingResult bindingResult, HttpServletRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(jwtLoginRequest));
	}

	@GetMapping("/bcrypt")
	public String test(@RequestParam(value = "secret", required = true) String secret) {
		return bCryptPasswordEncoder.encode(secret);
	}

	@GetMapping("/login/current-user")
	public LoginDto getCurrentUser(@AuthenticationPrincipal Staff user) {
		return convertToTableDto(user);
	}

	@GetMapping("/login/csrf")
	public CrsfTokenDto getToken(HttpServletRequest request) {
		CsrfToken csrf =  (CsrfToken) request.getAttribute("_csrf");
		return new CrsfTokenDto(csrf.getToken());
	}

	@PostMapping("/logout")
	public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

		logoutHandler.logout(request, response, authentication);

	    return "redirect:/success";
	}

	@GetMapping("/logout/success")
	public String success() {
		//this will be recap of daily sell
		return "logout";
	}
	
	private LoginDto convertToTableDto(Staff staff) {
		return modelMapper.map(staff, LoginDto.class);
	}

}
