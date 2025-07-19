package com.ucsy.springjwtauth.security;

import com.ucsy.springjwtauth.dtos.TokenPairs;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
@Slf4j
public class JwtService {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private long accessTokenExpirationMs = 1000 * 60;
    private long refreshTokenExpirationMs = 1000 * 60 * 60 * 24 * 3;

    private SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public TokenPairs generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPairs(accessToken, refreshToken);

    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenExpirationMs,new HashMap<>());
    }

    private String generateRefreshToken(Authentication authentication) {
        Map<String, String> claims = Map.of("token_type", "refresh_token");
        return generateToken(authentication, refreshTokenExpirationMs,claims);
    }

    private String generateToken(Authentication authentication, long expiresIn, Map<String, String> claims){
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiresIn);

        return Jwts.builder().
                header().add("typ", "jwt_token").
                and().
                claims(claims).
                subject(user.getUsername()).
                issuedAt(now).
                expiration(expiryDate).
                signWith(getSignKey())
                .compact();
    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null;
    }

    public boolean isValidTokenForUser(String token, UserDetails userDetails) {
        String username = extractAllClaims(token).getSubject();
        return username != null && username.equals(userDetails.getUsername());
    }

    public  boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("token_type").equals("refresh_token");
    }

    public String extractUsernameFromToken(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser().
                verifyWith(getSignKey()).build()
                .parseSignedClaims(token).getBody();

        return claims;
    }

}