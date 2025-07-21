package ru.dzhenbaz.AuthServiceT1.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token.expiration-ms}")
    private long refreshTokenExpirationMs;

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpirationMs, "access");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpirationMs, "refresh");
    }

    private String generateToken(UserDetails userDetails, long expirationMs, String type) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roles", userDetails.getAuthorities().stream()
                        .map(Object::toString).toList())
                .withClaim("type", type)
                .withIssuedAt(Date.from(now))
                .withIssuer("Dzhenbaz")
                .withExpiresAt(Date.from(now.plusMillis(expirationMs)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractUsername(String token) {
        return decode(token).getSubject();
    }

    private DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }

    public boolean isTokenExpired(String token) {
        return decode(token).getExpiresAt().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Instant extractExpiration(String token) {
        return decode(token).getExpiresAt().toInstant();
    }

    public String extractTokenType(String token) {
        return decode(token).getClaim("type").asString();
    }
}
