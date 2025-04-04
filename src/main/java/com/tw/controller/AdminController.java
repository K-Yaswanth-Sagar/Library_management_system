package com.tw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.dto.RegisterRequestDTO;
import com.tw.entity.User;
import com.tw.service.AuthenticationService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private AuthenticationService authenticationService;
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@PostMapping("/registeradminuser")
	public ResponseEntity<User> registerAdminnUser(@RequestBody RegisterRequestDTO registerRequestDTO){

		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    logger.info("Authenticated user: {}", authentication.getName());
		    logger.info("User roles: {}", authentication.getAuthorities());
		
		return ResponseEntity.ok(authenticationService.registerAdminUser(registerRequestDTO));
	}
	
}
