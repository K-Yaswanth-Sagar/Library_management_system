package com.tw.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tw.entity.User;
import com.tw.globalexceptionhandler.JwtValidationException;
import com.tw.repository.UserRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserRepo userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String username;
		
		String requestURI = request.getRequestURI(); 

		
		 if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
		        filterChain.doFilter(request, response);
		        return;
		    }
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		jwtToken = authHeader.substring(7);
		username = jwtService.extractUsername(jwtToken);
		if (username == null) {
	        throw new JwtValidationException("Invalid JWT token");
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User userDetails = userRepo.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));
			
			if(jwtService.isTokenValid(jwtToken, userDetails)) {
				
				List<SimpleGrantedAuthority> authorities = userDetails.getRoles()
																		.stream()
																		.map(SimpleGrantedAuthority :: new)
																		.collect(Collectors.toList());
				
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
																userDetails,
																null,
																authorities
						);
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
			}
		}
		filterChain.doFilter(request, response);
	}
	
}
