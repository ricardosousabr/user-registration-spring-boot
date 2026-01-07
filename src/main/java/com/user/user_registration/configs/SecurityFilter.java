package com.user.user_registration.configs;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.user.user_registration.models.User;
import com.user.user_registration.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var token = this.recoverToken(request);
		
		if (token != null) {
			DecodedJWT jwt = tokenService.validateTokenAndGetClaims(token);
			Long userId = jwt.getClaim("id").asLong();
			String role = jwt.getClaim("role").asString();
			
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
			
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(userId, null, authorities);
			
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
		}
		filterChain.doFilter(request, response);
	}
	
	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		
		if (authHeader == null) return null;
		return authHeader.replace("Bearer ", "");
	}
}

