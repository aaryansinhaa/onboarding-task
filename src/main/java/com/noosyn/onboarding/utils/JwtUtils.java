package com.noosyn.onboarding.utils;

import java.util.Date;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class responsible for generating and validating JWT tokens.
 * <p>
 * This component handles:
 * </p>
 * <ul>
 *     <li>Token creation using HS256 signing</li>
 *     <li>Token parsing and claim extraction</li>
 *     <li>Expiration validation</li>
 *     <li>Username extraction for authentication</li>
 * </ul>
 * 
 * <p>
 * Configuration properties:
 * </p>
 * <ul>
 *     <li>{@code jwt.secret} — Base64-encoded signing key</li>
 *     <li>{@code jwt.expirationMs} — token validity duration in milliseconds</li>
 * </ul>
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationMs}")
    private long expirationTime;

    /**
     * Converts the Base64-encoded secret into a {@link Key} suitable for HMAC signing.
     *
     * @return the cryptographic signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a signed JWT token for the given user.
     *
     * @param user the authenticated user for whom the token is created
     * @return a signed JWT token string
     */
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts all claims contained within the JWT token.
     *
     * @param token the JWT token to parse
     * @return the extracted claims
     * @throws io.jsonwebtoken.JwtException if the token is invalid or tampered with
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username stored in the token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validates the token by checking:
     * <ul>
     *     <li>The username matches the user details</li>
     *     <li>The token is not expired</li>
     * </ul>
     *
     * @param token        the JWT token to validate
     * @param userDetails  the user details to compare against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    /**
     * Checks whether the token has expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
