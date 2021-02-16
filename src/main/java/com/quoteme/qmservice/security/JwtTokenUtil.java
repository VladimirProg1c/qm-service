package com.quoteme.qmservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

	private String secret;

	private Long tokenTTL;

	public JwtTokenUtil(@Value("${jwt.secret}") String secret,
						@Value("${jwt.tokenTTL}") Long tokenTTL) {
		this.secret = secret;
		this.tokenTTL = tokenTTL;
	}

	public String getUserEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		try {
			final Date expiration = getExpirationDateFromToken(token);
			return expiration.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String generateToken(UserDetails userDetails) {
		Date currentTime = new Date(System.currentTimeMillis());
		Date expirationTime = new Date(System.currentTimeMillis() +  tokenTTL);

		return Jwts.builder()
				.setClaims(new HashMap<>())
				.setSubject(userDetails.getUsername())
				.setIssuedAt(currentTime)
				.setExpiration(expirationTime)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String email = getUserEmailFromToken(token);
		return Objects.equals(email, userDetails.getUsername());
	}

}
