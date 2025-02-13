package com.springSecurity.security.controllers;


import com.springSecurity.security.models.Users;
import com.springSecurity.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/register")
    public Users createUser(@RequestBody Users user){
        return service.createUser(user);
    }

    @PostMapping("/login")
    public String Login(@RequestBody Users user){
        return service.login(user);
    }

    @GetMapping("/csrf")
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken)request.getAttribute("_csrf");

    }

    @GetMapping("/users")
    public List<Users> allUsers(){
        return service.allUsers();
    }

    @GetMapping("/secure")
    public String secure(){
        return "Secure";
    }

    @GetMapping("/admin/page")
    public String admin(){
        return "Admin Page";
    }
    @GetMapping("/secure/user")
    public String secureUsers(){
        return "Secure Users";
    }

}
