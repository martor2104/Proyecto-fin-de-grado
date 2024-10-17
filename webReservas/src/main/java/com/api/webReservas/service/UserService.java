package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

	ResponseEntity<?> getAll(User loggedUser);
	ResponseEntity<?> getById(User loggedUser, Long id);
	ResponseEntity<?> deleteUser(User loggedUser, Long id);
	ResponseEntity<?>  putUser(User loggedUser, Long id, UserDTO user);
	ResponseEntity<?> addUser(User loggedUser, UserDTO user);
	User findByUsername(String username);
	Long getUserId();
}
