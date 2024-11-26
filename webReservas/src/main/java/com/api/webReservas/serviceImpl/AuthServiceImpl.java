package com.api.webReservas.serviceImpl;


import com.api.webReservas.entity.Role;

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
import com.api.webReservas.entity.User;
import com.api.webReservas.jwt.JwtService;
import com.api.webReservas.repository.UserRepository;
import com.api.webReservas.service.AuthService;

import java.util.HashMap;
import java.util.Map;

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

            // Verificar si userRepository está inicializado
            if (userRepository == null) {
                System.out.println("UserRepository is null in login method");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("UserRepository is null"));
            }

            // Autenticar al usuario usando el AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
            );

            // Buscar al usuario en el repositorio
            UserDetails userDetails = this.userRepository.findByOptionalName(request.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!(userDetails instanceof User)) {
                throw new IllegalStateException("UserDetails cannot be cast to User");
            }

            User user = (User) userDetails;

            // Crear los claims para el token JWT, incluyendo el ID del usuario
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().name());
            claims.put("name", user.getName());
            claims.put("userId", user.getId());
            claims.put("perfil", user.getPerfil());


            // Generar el token JWT con los claims

            String token = jwtService.getToken(claims, userDetails);

            // Devolver la respuesta con el token y la URL de la imagen de perfil
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token, user.getPerfil()));

        } catch (AuthenticationException e) {

            System.out.println("Authentication failed for user: " + request.getName() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Incorrect credentials"));
        } catch (Exception e) {

            System.out.println("An error occurred during login for user: " + request.getName() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("An error occurred"));
        }
    }




    @Override
    public ResponseEntity<?> register(User loggedUser, RegisterRequest request) {
        try {
            // Verificar si el rol solicitado es ADMIN y el usuario logueado no es administrador
            if ("ADMIN".equals(request.getRole()) && (loggedUser == null || loggedUser.getRole() != Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorDTO("Only administrators can register users with ADMIN role"));
            }

            // Si no es administrador, forzar el rol a USER
            if (!"ADMIN".equals(request.getRole())) {
                request.setRole("USER");
            }

            // Verificar duplicidad de nombre de usuario
            if (userRepository.existsByName(request.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorDTO("The username is already taken"));
            }

            // Verificar duplicidad de correo electrónico
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorDTO("The email is already registered"));
            }

            // Crear el nuevo usuario

            User newUser = new User(request);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");


            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            // Capturar excepciones genéricas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO("Failed to register user: " + e.getMessage()));
        }
    }


            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Failed to register user"));
        }
    }
}




}

