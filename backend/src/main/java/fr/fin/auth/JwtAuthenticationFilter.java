package fr.fin.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.fin.service.StaffService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private StaffService staffService;

	/**
	 * This method intercepts every request and check for the authorization header
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		/* We first check for the authentication header.
		 * If it doesn't exist or doesn't start with Bearer we abort the authentication process */
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = authHeader.substring(7); // Removing "Bearer" from the header
		String username = jwtService.extractUsername(jwt);

		/* We only process the authentication if the user is not authenticated yet
		 * It avoids useless processing for the server */
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails staff = staffService.loadUserByUsername(username);
			
			// We check the token validity, if it's valid, we can authenticate the user
			if(jwtService.isTokenValid(jwt, staff)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(staff, null, staff.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// We tell Spring Security to process the remaining FilterChain methods
		filterChain.doFilter(request, response);
	}
}
