package com.api.webReservas.serviceImpl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.webReservas.auth.AuthResponse;
import com.api.webReservas.auth.LoginRequest;
import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.jwt.JwtService;
import com.api.webReservas.repository.UserRepository;
import com.api.webReservas.service.AuthService;


public class AuthServiceImpl implements AuthService {
	
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

	@Override
	public ResponseEntity<?> login(LoginRequest request) {
		
		try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
            UserDetails user = userRepository.findByName(request.getName()).orElseThrow();

            Map<String, Object> claims = new HashMap<>();
            User userEntity = (User) user;
            claims.put("role", userEntity.getUserRol().name());
            claims.put("name", userEntity.getName());

            String token=jwtService.getToken(claims, user);

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Incorrect credentials"));
        }
	}

	@Override
	public ResponseEntity<?> register(User loggedUser, RegisterRequest user) {
		try {
			   if(loggedUser.getUserRol().equals(Role.ADMIN)) {
				   User newUser = new User(user);
				   newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
				   userRepository.save(newUser);
			        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
			   }else {
				   return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to save users"));
			   }
		}catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Failed to register user"));
		}

	}

}
