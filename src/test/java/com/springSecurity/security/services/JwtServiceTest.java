package com.springSecurity.security.services;

import com.springSecurity.security.models.CustomUserDetails;
import com.springSecurity.security.models.Users;
import com.springSecurity.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private Users user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        user = new Users("test", "test");
    }

    @Test
    void shouldGenerateTokenSuccessfully(){
        String token = jwtService.generateToken(user);

        System.out.println(token);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsernameFromToken(){
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals(user.getUsername(), username);
    }

    @Test
    void shouldValidateTokenSuccessfully(){

        String token = jwtService.generateToken(user);

        UserDetails userDetails = new CustomUserDetails(user);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);

    }

    @Test
    void shouldDetectExpiration(){

        Users expiredUser = new Users("expired", "expired");

        String token = jwtService.generateToken(expiredUser);

        try{
            Thread.sleep(1010); // adjust the time to make the token expire in JwtService by Tawsif :3
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(token));
    }
}