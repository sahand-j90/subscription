package com.example.subscription.security.jwt;

import com.example.subscription.enums.AuthenticationFlowEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Component
public class JwtTokenService {

    @Value("${subscription.security.jwt.secret-key}")
    private String secretKey;

    @Value("${subscription.security.jwt.expiration}")
    private Long expiration;

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String generateToken(AuthenticationFlowEnum authenticationFlowEnum, String username, Collection<? extends GrantedAuthority> authorities) {
        var claims = new HashMap<String, Object>();
        claims.put("roles", authorities);
        return createToken(authenticationFlowEnum, username, claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Claims extractAllClaims(String token) {

        var key = getKey();

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String createToken(AuthenticationFlowEnum authenticationFlow, String subject, Map<String, Object> claims) {

        var key = getKey();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(authenticationFlow.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}
