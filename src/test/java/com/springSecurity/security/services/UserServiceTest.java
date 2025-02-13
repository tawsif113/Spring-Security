package com.springSecurity.security.services;

import com.springSecurity.security.models.Users;
import com.springSecurity.security.repo.UserRepository;
import com.springSecurity.security.service.JwtService;
import com.springSecurity.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        testUser = new Users("test", "test");
    }

    @Test
    void shouldCreateUsersWithEncodedPassword(){

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        Users createdUser = userService.createUser(testUser);

        assertNotNull(createdUser);
        assertEquals("test",createdUser.getUsername());
        assertEquals("encodedPassword",createdUser.getPassword());

        verify(encoder,times(1)).encode("test");
        verify(userRepository,times(1)).save(testUser);
    }

    @Test
    void shouldReturnAllUsers() {
        // Given
        List<Users> mockUsers = Arrays.asList(
                new Users("user1", "password1"),
                new Users("user2", "password2")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<Users> users = userService.allUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldAuthenticateAndReturnJwtToken(){

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn("jwtToken");

        String jwtToken = userService.login(testUser);

        assertNotNull(jwtToken);
        assertEquals("jwtToken",jwtToken);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(testUser);
    }

}