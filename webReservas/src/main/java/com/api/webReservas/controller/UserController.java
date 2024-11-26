package com.api.webReservas.controller;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@PreAuthorize("hasRole('ADMIN')")
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

	@GetMapping("/id")
	public ResponseEntity<Long> getUserId(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails != null) {
			Long userId = ((User) userDetails).getId();
			return ResponseEntity.ok(userId);
		} else {
			return ResponseEntity.status(401).build();
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Borra un usuario", description = "Deshabilita un usuario de la base de datos por su id")
	public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
		return userService.deleteUser((User) userDetails, id);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Actualiza un usuario", description = "Actualiza un usuario por su id")
	public ResponseEntity<?> putUser(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id,
			@RequestPart("user") UserDTO user, // Cambiar @RequestBody por @RequestPart
			@RequestPart(value = "image", required = false) MultipartFile image) {
		return userService.putUser((User) userDetails, id, user, image);
	}

	@GetMapping("/api/auth/exists/username/{username}")
	public ResponseEntity<Boolean> verificarNombreUsuario(@PathVariable String username) {
		boolean existe = userService.existsByName(username);
		return ResponseEntity.ok(existe);
	}


	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addUser(
			@AuthenticationPrincipal UserDetails loggedUser,
			@RequestPart("user") UserDTO userDTO, // Cambiar @RequestBody por @RequestPart
			@RequestPart(value = "image", required = false) MultipartFile image) {
		return userService.addUser((User) loggedUser, userDTO, image);
	}

	@PostMapping(value = "/{id}/uploadProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadProfileImage(
			@PathVariable Long id,
			@RequestParam("image") MultipartFile image) {

		try {
			String imageUrl = userService.saveProfileImage(id, image);
			return ResponseEntity.ok(new MessageDTO("Imagen de perfil subida correctamente: " + imageUrl));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDTO("Error al subir la imagen de perfil"));
		}
	}
}

