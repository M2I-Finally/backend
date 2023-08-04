package fr.fin.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	// https://jwt-keys.21no.de/
	private static final String SECRET_KEY = "0NK97McIAUPfeg5C3SLIDpiFYcicW/JM3cE0KVAKgqLqdq5L2kSA/K8xR4Hzmdff8zJk7eSIAcvcAmjQH9umMw==";

	/**
	 * Extract the username ("subject" attribute) from the JWT Token
	 * @param token				JWT Token
	 * @return					The username issued in the token
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extract a target claim from the JWT Token
	 * @param token				JWT Token
	 * @param claimResolver		Method reference for the desired target claim
	 * @return					The claim of type <T> 
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	/**
	 * Generate JWT token without extra claims
	 * @param userDetails		The UserDetails class (in our implementation it's staff)
	 * @return					The generated JWT Token
	 */
	public String generateToken(UserDetails userDetails) {
		return this.generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * Generate JWT Token with extra claims in the payload part of the token
	 * @param extraClaims		Map<String, Object> containing every extra claims
	 * @param userDetails		The UserDetails class (in our implementation it's staff)
	 * @return					The generated JWT Token
	 */
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.
				builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	/**
	 * Helper method that checks for the JWT token validity
	 * It checks the username and if the token is expired or not
	 * @param token				The JWT Token to check
	 * @param userDetails		The UserDetails class (in our implementation it's staff)
	 * @return					A boolean representing the validation state
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * Helper method that checks for the JWT Token Expiration
	 * @param token				The JWT Token
	 * @return					A boolean representing the expiration state
	 */
	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	/**
	 * Helper method that extracts all claims in the JWT Token
	 * @param token				The JWT Token
	 * @return					Set<Claims> with every claims found in the JWT Token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

}
