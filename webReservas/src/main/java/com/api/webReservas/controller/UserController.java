package com.api.webReservas.controller;

import com.api.webReservas.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@GetMapping()
	@Operation(summary = "Obtener todos los usuarios", description = "Devuelve todos los usuarios")
	public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
		return userService.getAll((User) userDetails);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener un usuario", description = "Obtiene un usuario por id")
	public ResponseEntity<?> getUserById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
		return userService.getById((User) userDetails, id);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Borra un usuario", description = "Deshabilita un usuario de la base de datos por su id")
	public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
		return userService.deleteUser((User) userDetails, id);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualiza un usuario", description = "Actualiza un usuario por su id")
	public ResponseEntity<?> putUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody UserDTO user) {
		return userService.putUser((User) userDetails, id, user);
	}


}
