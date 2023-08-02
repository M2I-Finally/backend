package fr.fin.service;

import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	// https://jwt-keys.21no.de/
	private static final String SECRET_KEY = """
		jrs68jr0aCOPqscJRQCjb+q/0vRkdiuZGj1JXWVqZ8lwHPWXEoIaBqdIHI7WgvyG
		cfWQFDjOg0kL/6R7C2Ey3g==
	""";

	public String extractUsername(String token) {
		return null;
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
