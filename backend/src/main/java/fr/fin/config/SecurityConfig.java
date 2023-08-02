package fr.fin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@SuppressWarnings("removal")
	@Bean
	  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	        .authorizeHttpRequests(customizer -> customizer
	            .requestMatchers("/login").permitAll()
	            .requestMatchers("/login/csrf").permitAll()
	            .requestMatchers("/**").authenticated())
	            
	        .csrf().ignoringRequestMatchers("/login", "/logout").and()
	        .logout((logout) -> logout
	                .logoutSuccessUrl("/logout/success")
	                .permitAll())
	        .exceptionHandling(customizer -> customizer
	            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
	        .build();
	  }
}
