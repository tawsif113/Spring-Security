package com.springSecurity.security.service;

import com.springSecurity.security.models.Users;
import com.springSecurity.security.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository repository, BCryptPasswordEncoder encoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public Users createUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public List<Users> allUsers() {
        return repository.findAll();
    }

    public String login(Users user) {


        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );

        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(user);
        } else {
            return "User not found";
        }
    }
}
