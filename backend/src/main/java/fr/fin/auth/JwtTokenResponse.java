package fr.fin.auth;

/**
 * If the authentication is successful, the result returned is of type JwtTokenResponse
 * that contains the generated token.
 */
public class JwtTokenResponse {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JwtTokenResponse(String token) {
		this.token = token;
	}

	public JwtTokenResponse() {
	}

}
