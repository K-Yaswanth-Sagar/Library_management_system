package com.tw.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.tw.entity.User;
import com.tw.globalexceptionhandler.TokenExpiredException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String secretKey = "YXNkZmdoamtscXdlcnR5dWlvcGFzZGZnYXNkZmdoamtsYQ==";
    private long expiration = 1000 * 60 * 60; 

    private String token;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "expiration", expiration);

        user = new User();
        user.setUsername("testuser");

        when(userDetails.getUsername()).thenReturn("testuser");

        token = Jwts.builder()
                .subject(userDetails.getUsername())  
                .issuedAt(new Date(System.currentTimeMillis()))  
                .expiration(new Date(System.currentTimeMillis() + expiration))  
                .signWith(getSignInKey())
                .compact();

    }


    private SecretKey getSignInKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtService.generateToken(userDetails);
        assertNotNull(generatedToken);
        assertTrue(generatedToken.length() > 0);
    }

    @Test
    void testExtractUsername() {
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testIsTokenValid() {
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void testIsTokenExpired() {
        assertFalse(jwtService.isTokenExpired(token)); 
    }

    @Test
    void testExtractExpiration() {
        Date expirationDate = jwtService.extractExpiration(token);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testExpiredTokenThrowsException() {
       String expiredToken = Jwts.builder()
                .claims().subject(userDetails.getUsername()).and()
                .issuedAt(new Date(System.currentTimeMillis() - 10000))  
                .expiration(new Date(System.currentTimeMillis() - 5000)) 
                .signWith(getSignInKey())
                .compact();



        TokenExpiredException exception = assertThrows(TokenExpiredException.class, () -> {
            jwtService.extractUsername(expiredToken);
        });

        assertEquals("Token is expired", exception.getMessage());
    }
}
