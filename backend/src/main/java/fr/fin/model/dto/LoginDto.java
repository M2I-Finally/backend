package fr.fin.model.dto;

import org.springframework.security.web.csrf.CsrfToken;

public class LoginDto {

	//user.getId(), user.getUsername(), user.getRole()
	private Integer id;
	private String username;
	private String role;
	private CsrfToken token;

	public LoginDto() {
	}

	public LoginDto(Integer id, String username, String role, CsrfToken token) {
		this.id = id;
		this.username = username;
		this.role = role;
		this.token = token;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public CsrfToken getToken() {
		return token;
	}

	public void setToken(CsrfToken csrf) {
		this.token = csrf;
	}



}
