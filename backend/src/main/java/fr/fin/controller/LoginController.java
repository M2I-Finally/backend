package fr.fin.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.fin.model.dto.LoginDto;
import fr.fin.model.dto.LoginForm;
import fr.fin.model.entity.Staff;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ModelMapper modelMapper;
	
	private LoginDto convertToTableDto(Staff staff) {
		return modelMapper.map(staff, LoginDto.class);
	}
	
	private Staff convertToStaff(LoginDto loginDto) {
		return modelMapper.map(loginDto, Staff.class);
	}
	
	@PostMapping
	public LoginDto login(@Valid @RequestBody LoginForm form, BindingResult bindingResult,
	                         HttpServletRequest request) throws Exception {
	  if (bindingResult.hasErrors()) {
	    throw new Exception("Invalid username or password");
	  }

	  try {
	    request.login(form.getUsername(), form.getPassword());
	  } catch (ServletException e) {
	    throw new Exception("Invalid username or password");
	  }

	  var auth = (Authentication) request.getUserPrincipal();
	  var user = (Staff) auth.getPrincipal();
	  
	  System.out.println("User {} logged in."+ user.getUsername());
	  
	  return convertToTableDto(user);
	}
	
	
	@GetMapping
	public String test() {
		String pass = bCryptPasswordEncoder.encode("admin");
		return pass;
	}
	
	@GetMapping("/current-user")
	public LoginDto getCurrentUser(@AuthenticationPrincipal Staff user) {
//	  need role after
	  return convertToTableDto(user);
	}
}
