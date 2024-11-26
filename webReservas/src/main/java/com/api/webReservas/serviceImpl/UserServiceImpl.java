package com.api.webReservas.serviceImpl;


import com.api.webReservas.dto.SuccessDTO;
import com.api.webReservas.entity.Reservation;
import com.api.webReservas.entity.Table;

import com.api.webReservas.jwt.JwtUserDetails;
import com.api.webReservas.repository.ReservationRepository;
import com.api.webReservas.repository.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.UserRepository;
import com.api.webReservas.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private TableRepository tableRepository;

	private final String directoryPath = "E:/TFG/Front/Proyecto-fin-de-grado/FrontReservas/webReservas/src/assets/img_perfil";


	@Override
	public ResponseEntity<?> getAll(User loggedUser) {
		if(loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll().stream().map(User::toDTO));
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to list users"));
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
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to list users"));
		}
	}
	public String saveProfileImage(Long userId, MultipartFile image) throws IOException {
		String filePath = Paths.get(directoryPath, image.getOriginalFilename()).toString();
		Files.copy(image.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

		// Actualizar el atributo perfil en el usuario
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		String imageUrl = "assets/img_perfil/" + image.getOriginalFilename();
		user.setPerfil(imageUrl); // Guarda la URL de la imagen en el atributo perfil
		userRepository.save(user);

		return imageUrl;
	}


	@Transactional
	@Override
	public ResponseEntity<?> deleteUser(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User user = userRepository.findById(id).orElse(null);


			if (user != null) {
				// Obtiene todas las reservas del usuario
				List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
				if (reservations != null && !reservations.isEmpty()) {
					for (Reservation reservation : reservations) {
						// Encuentra la mesa asociada a la reserva y pone la referencia en null
						Table table = tableRepository.findByReservationId(reservation.getId());
						if (table != null) {
							table.setReservation(null);
							tableRepository.save(table);  // Guarda la mesa actualizada
						}

						// Elimina la reserva después de actualizar la mesa
						reservationRepository.delete(reservation);
					}
				}

				// Finalmente, elimina el usuario
				userRepository.delete(user);
				return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("User and associated reservations deleted"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You don't have permissions to delete users"));
		}
	}


	@Override

	public ResponseEntity<?> putUser(User loggedUser, Long id, UserDTO userDTO, MultipartFile image) {
		// Si el usuario no es ADMIN, se asegura de que solo pueda actualizar su propio perfil
		if (!loggedUser.getId().equals(id) && !loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("No tienes permisos para modificar este usuario"));
		}

		// Buscar al usuario en la base de datos
		User oldUser = userRepository.findById(id).orElse(null);

		if (oldUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorDTO("El usuario no existe"));
		}


		// Actualizar los campos si no son nulos
		oldUser.setName(userDTO.getName() != null ? userDTO.getName() : oldUser.getName());
		oldUser.setEmail(userDTO.getEmail() != null ? userDTO.getEmail() : oldUser.getEmail());

		// Si el usuario es un ADMIN, permitir cambiar el rol
		if (loggedUser.getRole().equals(Role.ADMIN) && userDTO.getUserRol() != null) {
			oldUser.setRole(userDTO.getUserRol());
		}

		// Actualizar la contraseña si se proporciona
		if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
			oldUser.setPassword(encodedPassword);
		}

		// Si se proporciona una nueva imagen de perfil, actualizarla
		if (image != null && !image.isEmpty()) {
			try {
				String imageUrl = saveProfileImage(oldUser.getId(), image);
				oldUser.setPerfil(imageUrl);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ErrorDTO("Error al actualizar la imagen de perfil."));
			}
		}

		// Guardar los cambios en la base de datos
		userRepository.save(oldUser);

		return ResponseEntity.ok(new SuccessDTO("Usuario actualizado correctamente."));
	}




	@Override
	public ResponseEntity<?> addUser(User loggedUser, UserDTO userDTO, MultipartFile image) {
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("No tienes permisos para añadir un usuario.");
		}

		if (userRepository.existsByEmail(userDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Ya existe un usuario con este email.");
		}

		User newUser = new User();
		newUser.setName(userDTO.getName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setRole(userDTO.getUserRol().equals(Role.ADMIN) ? Role.ADMIN : Role.USER);

		// Guardar la imagen de perfil si se proporciona
		if (image != null && !image.isEmpty()) {
			try {
				String imageUrl = saveProfileImage(newUser.getId(), image);
				newUser.setPerfil(imageUrl);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error al guardar la imagen de perfil.");
			}
		}

		userRepository.save(newUser);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Usuario añadido correctamente.");
	}


	@Override
	public User findByUsername(String username) {
		Optional<User> userOptional = userRepository.findByOptionalName(username);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return user;
		} else {
			return null;
		}
	}

	@Override
	public Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
			JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
			return userDetails.getId();
		}
		return null;
	}

	@Override
	public ResponseEntity<?> addUser(User loggedUser, UserDTO userDTO) {
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("No tienes permisos para añadir un usuario.");
		}

		if (userRepository.existsByEmail(userDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Ya existe un usuario con este email.");
		}

		User newUser = new User();
		newUser.setName(userDTO.getName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setRole(userDTO.getUserRol().equals(Role.ADMIN) ? Role.ADMIN : Role.USER);


		userRepository.save(newUser);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Usuario añadido correctamente.");
	}

	@Override
	public User findByUsername(String username) {
		Optional<User> userOptional = userRepository.findByOptionalName(username);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return user;
		} else {
			return null;
		}
	}

	@Override
	public Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
			JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
			return userDetails.getId();
		}
		return null;
	}

}
