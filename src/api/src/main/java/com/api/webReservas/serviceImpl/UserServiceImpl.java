package com.api.webReservas.serviceImpl;

import com.api.webReservas.dto.*;
import com.api.webReservas.entity.*;
import com.api.webReservas.jwt.JwtUserDetails;
import com.api.webReservas.repository.*;
import com.api.webReservas.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private TableRepository tableRepository;

	// Ruta base para las imágenes de perfil
	private final Path baseDirectory = Paths.get(System.getProperty("user.dir"), "..", "FrontReservas","webReservas", "src", "Images", "assets", "img_perfil").normalize();

	@Override
	public ResponseEntity<?> getAll(User loggedUser) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(userRepository.findAll().stream().map(User::toDTO));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("You don't have permissions to list users"));
		}
	}

	@Override
	public boolean existsByName(String name) {
		return userRepository.existsByName(name);
	}

	@Override
	public ResponseEntity<?> getById(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User user = userRepository.findById(id).orElse(null);

			if (user != null) {
				return ResponseEntity.status(HttpStatus.OK).body(User.toDTO(user));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDTO("User doesn't exist"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("You don't have permissions to access this user"));
		}
	}

	public String saveProfileImage(Long userId, MultipartFile image) throws IOException {
		// Crear el directorio si no existe
		if (!Files.exists(baseDirectory)) {
			Files.createDirectories(baseDirectory);
		}

		// Validar que el archivo no sea nulo ni vacío
		if (image == null || image.isEmpty()) {
			throw new IllegalArgumentException("The provided image file is empty or null.");
		}

		// Validar que el nombre del archivo sea seguro
		String originalFilename = image.getOriginalFilename();
		if (originalFilename == null || originalFilename.trim().isEmpty()) {
			throw new IllegalArgumentException("The image file does not have a valid name.");
		}
		if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
			throw new IllegalArgumentException("The file name contains unsafe characters.");
		}

		// Limpiar el nombre del archivo para mayor seguridad
		String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

		// Construir la ruta del archivo
		Path filePath = baseDirectory.resolve(sanitizedFilename);

		// Guardar el archivo en el sistema
		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Log de éxito
		System.out.println("File saved successfully at: " + filePath.toAbsolutePath());

		// Actualizar el atributo perfil en el usuario
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Usar solo el nombre del archivo como URL relativa para el frontend
		String imageUrl = sanitizedFilename; // Devuelve solo el nombre del archivo
		System.out.println("Image URL sent to frontend: " + imageUrl);
		user.setPerfil(imageUrl);
		userRepository.save(user);

		return imageUrl;
	}


	@Transactional
	@Override
	public ResponseEntity<?> deleteUser(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User user = userRepository.findById(id).orElse(null);

			if (user != null) {
				// Eliminar las reservas asociadas y actualizar las mesas relacionadas
				List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
				if (reservations != null && !reservations.isEmpty()) {
					for (Reservation reservation : reservations) {
						Table table = tableRepository.findByReservationId(reservation.getId());
						if (table != null) {
							table.setReservation(null);
							tableRepository.save(table);
						}
						reservationRepository.delete(reservation);
					}
				}

				// Finalmente, eliminar el usuario
				userRepository.delete(user);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new MessageDTO("User and associated reservations deleted"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDTO("User doesn't exist"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("You don't have permissions to delete users"));
		}
	}

	@Override
	public ResponseEntity<?> putUser(User loggedUser, Long id, UserDTO userDTO, MultipartFile image) {
		// Verificar permisos
		if (!loggedUser.getId().equals(id) && !loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("You don't have permissions to modify this user"));
		}

		// Buscar al usuario
		User oldUser = userRepository.findById(id).orElse(null);

		if (oldUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorDTO("User doesn't exist"));
		}

		// Actualizar datos
		oldUser.setName(userDTO.getName() != null ? userDTO.getName() : oldUser.getName());
		oldUser.setEmail(userDTO.getEmail() != null ? userDTO.getEmail() : oldUser.getEmail());

		if (loggedUser.getRole().equals(Role.ADMIN) && userDTO.getUserRol() != null) {
			oldUser.setRole(userDTO.getUserRol());
		}

		if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
			oldUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		}

		if (image != null && !image.isEmpty()) {
			try {
				String imageUrl = saveProfileImage(oldUser.getId(), image);
				oldUser.setPerfil(imageUrl);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ErrorDTO("Error updating profile image"));
			}
		}

		userRepository.save(oldUser);
		return ResponseEntity.ok(new SuccessDTO("User updated successfully"));
	}

	@Override
	public ResponseEntity<?> addUser(User loggedUser, UserDTO userDTO, MultipartFile image) {
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("You don't have permissions to add a user"));
		}

		if (userRepository.existsByEmail(userDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDTO("User with this email already exists"));
		}

		User newUser = new User();
		newUser.setName(userDTO.getName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setRole(userDTO.getUserRol().equals(Role.ADMIN) ? Role.ADMIN : Role.USER);

		if (image != null && !image.isEmpty()) {
			try {
				String imageUrl = saveProfileImage(newUser.getId(), image);
				newUser.setPerfil(imageUrl);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ErrorDTO("Error saving profile image"));
			}
		}

		userRepository.save(newUser);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new SuccessDTO("User added successfully"));
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByOptionalName(username).orElse(null);
	}

	@Override
	public Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
			return ((JwtUserDetails) authentication.getPrincipal()).getId();
		}
		return null;
	}
}
