package com.tw.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tw.dto.LoginRequestDTO;
import com.tw.dto.LoginResponseDTO;
import com.tw.dto.RegisterRequestDTO;
import com.tw.entity.User;
import com.tw.globalexceptionhandler.ResourceNotFoundException;
import com.tw.jwt.JwtService;
import com.tw.repository.UserRepo;


@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	public User registerNormalUser(RegisterRequestDTO registerRequestDTO) {

		if(userRepo.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
			throw new RuntimeException("User already registred");
		}
		
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_USER");
		
		User user = new User();
		user.setEmail(registerRequestDTO.getEmail());
		user.setUsername(registerRequestDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user.setRoles(roles);
		
		return userRepo.save(user );
	}

	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequestDTO.getUsername(),
						loginRequestDTO.getPassword())
				);
		User user = userRepo.findByUsername(loginRequestDTO.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		String token = jwtService.generateToken(user);
		
		return LoginResponseDTO.builder()
				.token(token)
				.username(user.getUsername())
				.roles(user.getRoles())
				.build();
	}

	public User registerAdminUser(RegisterRequestDTO registerRequestDTO) {
		if(userRepo.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
			throw new RuntimeException("User already registred");
		}
		
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_USER");
		roles.add("ROLE_ADMIN");
		
		User user = new User();
		user.setEmail(registerRequestDTO.getEmail());
		user.setUsername(registerRequestDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		user.setRoles(roles);
		
		
		return userRepo.save(user);
	}

}
