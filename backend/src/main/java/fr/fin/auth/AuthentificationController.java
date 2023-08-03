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
import org.springframework.web.bind.annotation.RestController;

import fr.fin.model.dto.AuthenticationResponse;
import fr.fin.model.dto.CrsfTokenDto;
import fr.fin.model.dto.JwtLoginDto;
import fr.fin.model.dto.LoginDto;
import fr.fin.model.dto.LoginForm;
import fr.fin.model.entity.Staff;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class AuthentificationController {

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationService authenticationService;

	private LoginDto convertToTableDto(Staff staff) {
		return modelMapper.map(staff, LoginDto.class);
	}

	private Staff convertToStaff(LoginDto loginDto) {
		return modelMapper.map(loginDto, Staff.class);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> jwtLogin(@RequestBody JwtLoginDto form, BindingResult bindingResult, HttpServletRequest request)
			throws Exception {
		return ResponseEntity.ok(authenticationService.authenticate(form));
	}

	@GetMapping("/login")
	public String test() {
		String pass = bCryptPasswordEncoder.encode("admin");
		return pass;
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

}
