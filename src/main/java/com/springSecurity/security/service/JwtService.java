package com.springSecurity.security.service;

import com.springSecurity.security.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String KEY = "305bae03b37b67d6339a86a1d6aa83c88a0383c3449312d773fae1b85eb2a704646007b68fa3fbbbdedfc419e7bc9e6036d582acfa599e436b1e33057c14e8a08472e3d9ffe027cfb784dfdb16305b1c29fcc797a8d1b6d55fd24a1b92255934e0ec90bac46822d82433ab253f87e59c57e4e8166c6fb3e977bb09293eb268138fa9074431f0e0ca66120d6ff95e668fd482f0a8479b4e82dfb0b0a6331de505629eec88105a58735a29b06d4004a18c39804f4544c892af58c0b1c54657424a9f9191594edc80fcbf2464459572e1f0cab781a61440a8ee15e1d3bb3647f4304ab0a1aac758e7681709e25915b7ddc4e8bee6ebbd510d18c9811ba182b91c50";

    public String generateToken(Users user) {

        Map<String,Object> claims= new HashMap<>();
        claims.put("role", "ROLE_"+user.getRole());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuer("abc")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] bytes = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractUsername(String token) {

        return extractClaims(token, Claims::getSubject);

    }

    private <T> T extractClaims(String token, Function<Claims,T> ClaimsResolver) {
        final Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {

        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaims(jwt, Claims::getExpiration);
    }
}
