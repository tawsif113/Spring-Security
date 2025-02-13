package com.springSecurity.security.services;

import com.springSecurity.security.models.Users;
import com.springSecurity.security.repo.UserRepository;
import com.springSecurity.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameSuccessfully() {

        Users mockUser = new Users("test", "test");
        when(repository.findByUsername("test")).thenReturn(mockUser);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test");

        assertNotNull(userDetails);
        assertEquals("test", userDetails.getUsername());
        assertEquals("test", userDetails.getPassword());
        verify(repository, times(1)).findByUsername("test");
    }

    @Test
    void loadUserByUsernameUserNotFound() {

        when(repository.findByUsername("Unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("Unknown"));
        verify(repository, times(1)).findByUsername("Unknown");
    }

}