package com.api.webReservas.controller;

import com.api.webReservas.entity.Role;
import com.api.webReservas.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.api.webReservas.auth.LoginRequest;
import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println(request.getName());
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RegisterRequest user) {
        User loggedUser = (userDetails != null) ? (User) userDetails : null;
        return authService.register(loggedUser, user);
    }

    @GetMapping("/isActive")
    public ResponseEntity<Boolean> checkUserIsActive(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        boolean isActive = user.isEnabled();
        return ResponseEntity.ok(isActive);
    }

    @GetMapping("/isLoggedIn")
    public Long getUserId() {
        return userService.getUserId();
    }
}
