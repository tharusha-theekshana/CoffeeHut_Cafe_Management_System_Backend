package com.cafe_mn_system.coffeehut_backend.Jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {

    @Value("${secret}")
    private String secret;

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(secret) // Use your secret or key
                .build(); // Build the parser
        Jws<Claims> jws = parser.parseClaimsJws(token); // Parse the token and get the claims
        return jws.getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String tokenGenerate(String username,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return generateToken(claims,username);
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        System.out.println("secret" + secret);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetail) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetail.getUsername()) && !isTokenExpired(token));
    }

}
