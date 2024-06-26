package com.api.webReservas.serviceImpl;

import com.api.webReservas.jwt.JwtUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.UserRepository;
import com.api.webReservas.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository repository;

	@Override
	public ResponseEntity<?> getAll(User loggedUser) {
		if(loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.OK).body(repository.findAll().stream().map(User::toDTO));
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to list users"));
		}	
	}

	@Override
	public ResponseEntity<?> getById(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {

			User user = repository.findById(id).orElse(null);


			if (user != null) {
				return ResponseEntity.status(HttpStatus.OK).body(User.toDTO(user));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to list users"));
		}
	}

	@Override
	public ResponseEntity<?> deleteUser(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User user = repository.findById(id).orElse(null);

			if(user != null) {
				repository.delete(user);
				return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("User deleted"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to delete users"));
		}
	}

	@Override
	public ResponseEntity<?> putUser(User loggedUser, Long id, UserDTO user) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			User oldUser = repository.findById(id).orElse(null);

			if (oldUser != null) {

				oldUser.setName((user.getName() != null) ? user.getName() : oldUser.getName());
				oldUser.setRole((user.getUserRol() != null) ? user.getUserRol() : oldUser.getRole());
				oldUser.setEmail((user.getEmail() != null) ? user.getEmail() : oldUser.getEmail());

				return ResponseEntity.status(HttpStatus.OK).body(User.toDTO(repository.save(oldUser)));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to modify user"));
		}
	}

	@Override
	public User findByUsername(String username) {
		// Buscar el usuario en la base de datos por su nombre de usuario
		Optional<User> userOptional = repository.findByName(username);

		// Verificar si el usuario existe en la base de datos
		if (userOptional.isPresent()) {
			User user = userOptional.get(); // Obtener el objeto User del Optional
			return user;
		} else {
			return null; // Devolver null si no se encuentra el usuario
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
