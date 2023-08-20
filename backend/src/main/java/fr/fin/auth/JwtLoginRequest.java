package fr.fin.auth;

/**
 * JwtLoginRequest represents the needed informations for JWT Login
 * It needs a username and a password
 */
public class JwtLoginRequest {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JwtLoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public JwtLoginRequest() {
	}

}

