package org.example.ratingsystem.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims) // Optional: Add extra claims (like roles)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME))
                .signWith(getSecretSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return !isTokenExpired(token) && username.equals(userDetails.getUsername());
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(Date.from(Instant.now()));
    }

    private SecretKey getSecretSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
