package fr.fin.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.fin.service.JwtService;
import fr.fin.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	// We can intercept request and provide new data for each new request
	// FilterChain contains the other filter from SecurityConfig

	@Autowired
	private JwtService jwtService;

	@Autowired
	private LoginService loginService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String jwt;
		String username;

		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);
		System.out.println(jwt);
		username = jwtService.extractUsername(jwt);

		// Check if user is not connected yet
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails staff = this.loginService.loadUserByUsername(username);
			if(jwtService.isTokenValid(username, staff)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(staff, null, staff.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}
}
