package com.takarub.securityPermissions.service;

import com.takarub.securityPermissions.model.User;
import com.takarub.securityPermissions.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;
    private final String SECRET_HEY =
            "61cb129e2175c2a9d0b387bca1e2fd3d2bc4cbcc781d12ed8f6d9c7734da84d5";

    public boolean isValid(String token, UserDetails user) {

        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByToken(token)
                .map(token1 -> !token1.isLoggedOut()).orElse(false);
        return (username.equals(user.getUsername())&& !isTokenExpired(token) && isValidToken);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClimes(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClimes(token , Claims::getSubject);

    }
    private <T> T extractClimes(String token , Function<Claims,T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateToken(User user) {
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000 ))
                .signWith(getSigninKey())
                .compact();

        return token;
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_HEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
