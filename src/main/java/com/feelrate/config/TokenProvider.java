package com.feelrate.config;

import com.feelrate.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime()+expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        SecretKey secretKey = new SecretKeySpec(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validToken(String token){
        try{
            SecretKey secretKey = new SecretKeySpec(
                    jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                    SignatureAlgorithm.HS256.getJcaName()
            );
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security
                .core.userdetails
                .User(claims.getSubject(), "", authorities), token, authorities);
    }
    public Long getUserId(String token){
     Claims claims = getClaims(token);
     return claims.get("id", Long.class);
    }
    private Claims getClaims(String token) {
        SecretKey secretKey = new SecretKeySpec(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}