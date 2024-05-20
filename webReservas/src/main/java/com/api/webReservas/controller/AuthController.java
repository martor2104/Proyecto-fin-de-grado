package com.api.webReservas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.webReservas.auth.LoginRequest;
import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

	
    private AuthService authService;

    /**
     *
     Method to log in with a user
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Method to register a user
     * @param userDetails
     * @param user
     * @return
     */
    @PostMapping("/register")
    @SecurityRequirement(name = "Authorized")
    public ResponseEntity<?> register(@AuthenticationPrincipal UserDetails userDetails, @RequestBody RegisterRequest user) {
        return authService.register((User) userDetails, user);
    }
    
}
