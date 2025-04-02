package com.tw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.dto.LoginRequestDTO;
import com.tw.dto.LoginResponseDTO;
import com.tw.dto.RegisterRequestDTO;
import com.tw.entity.User;
import com.tw.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	 private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	    @Autowired
	    private AuthenticationService authenticationService;
	    
	    @PostMapping("/registernormaluser")
	    public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
	        logger.info("Registering normal user: {}", registerRequestDTO.getUsername());
	        User user = authenticationService.registerNormalUser(registerRequestDTO);
	        logger.info("User registered successfully: {}", user.getUsername());
	        return ResponseEntity.ok(user);
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
	        logger.info("User attempting to login: {}", loginRequestDTO.getUsername());
	        LoginResponseDTO response = authenticationService.login(loginRequestDTO);
	        logger.info("User logged in successfully: {}", response.getUsername());
	        return ResponseEntity.ok(response);
	    }
	
}
