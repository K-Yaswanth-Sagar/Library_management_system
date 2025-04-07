package com.tw.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

	private String token;
	private String username;
	private String roles;
	
}
