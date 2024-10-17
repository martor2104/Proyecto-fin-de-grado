package com.api.webReservas.serviceImpl;

import com.api.webReservas.entity.Reservation;
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

	@Override
	public ResponseEntity<?> getAll(User loggedUser) {
		if(loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll().stream().map(User::toDTO));
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to list users"));
		}	
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

	@Transactional
	@Override
	public ResponseEntity<?> deleteUser(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User user = userRepository.findById(id).orElse(null);

			if (user != null) {
				List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
				if (reservations != null && !reservations.isEmpty()) {
					for (Reservation reservation : reservations) {
						tableRepository.deleteByReservationId(reservation.getId());

						reservationRepository.delete(reservation);
					}
				}

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
	public ResponseEntity<?> putUser(User loggedUser, Long id, UserDTO user) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User oldUser = userRepository.findById(id).orElse(null);

			if (oldUser != null) {

				oldUser.setName((user.getName() != null) ? user.getName() : oldUser.getName());
				oldUser.setRole((user.getUserRol() != null) ? user.getUserRol() : oldUser.getRole());
				oldUser.setEmail((user.getEmail() != null) ? user.getEmail() : oldUser.getEmail());

				return ResponseEntity.status(HttpStatus.OK).body(User.toDTO(userRepository.save(oldUser)));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to modify user"));
		}
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
