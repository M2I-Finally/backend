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
	private final String SECRET_KEY = "0NK97McIAUPfeg5C3SLIDpiFYcicW/JM3cE0KVAKgqLqdq5L2kSA/K8xR4Hzmdff8zJk7eSIAcvcAmjQH9umMw==";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return this.generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.
				builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 12))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

//	private Key getSignInKey() {
//		return Keys.hmacShaKeyFor(SECRET_KEY);
//	}
}
