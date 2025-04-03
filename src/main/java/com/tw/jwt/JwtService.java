package com.tw.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.tw.entity.User;
import com.tw.globalexceptionhandler.TokenExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secretkey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts
				.builder()
				.claims(extraClaims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey())
				.compact();
	}

	public String extractUsername(String jwtToken) {
		return extractClaims(jwtToken, Claims::getSubject);
	}
	
	private <T> T extractClaims(String jwtToken, Function<Claims, T> claimResolver) {
		final Claims claims = extreactAllClaims(jwtToken);
		return claimResolver.apply(claims);
	}

	private Claims extreactAllClaims(String jwtToken) {
		try {
			return Jwts
					.parser()
					.verifyWith(getSignInKey())
					.build()
					.parseSignedClaims(jwtToken)
					.getPayload();
		}
		catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token is expired");
        } catch (JwtException e) {  // Catch any other JWT exceptions
            throw new JwtException("Invalid JWT token");
        }
		
	}
	
	private SecretKey getSignInKey() {
	    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isTokenValid(String jwtToken, User userDetails) {
		final String username = extractUsername(jwtToken);
		return (userDetails.getUsername().equals(username) && !isTokenExpired(jwtToken));
	}

	public boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	public Date extractExpiration(String jwtToken) {
		return extractClaims(jwtToken, Claims :: getExpiration);
	}

}
