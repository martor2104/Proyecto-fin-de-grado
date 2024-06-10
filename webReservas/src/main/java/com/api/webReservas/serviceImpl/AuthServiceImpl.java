package com.api.webReservas.serviceImpl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.webReservas.auth.AuthResponse;
import com.api.webReservas.auth.LoginRequest;
import com.api.webReservas.auth.RegisterRequest;
import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.jwt.JwtService;
import com.api.webReservas.repository.UserRepository;
import com.api.webReservas.service.AuthService;

@Service
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
            // Autenticar al usuario usando el AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
            );

            // Buscar al usuario en el repositorio
            UserDetails userDetails = userRepository.findByName(request.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            System.out.println(userDetails);

            // Verificar que el UserDetails se pueda convertir a User
            if (!(userDetails instanceof User)) {
                throw new IllegalStateException("UserDetails cannot be cast to User");
            }

            User user = (User) userDetails;

            // Crear los claims para el token JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().name());
            claims.put("name", user.getName());

            // Generar el token JWT
            String token = jwtService.getToken(claims, userDetails);

            // Devolver la respuesta con el token
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Incorrect credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("An error occurred"));
        }
    }


    @Override
	public ResponseEntity<?> register(User loggedUser, RegisterRequest user) {
		try {
				   User newUser = new User(user);
				   newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
				   userRepository.save(newUser);
			        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
		}catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Failed to register user"));
		}

	}
    }


