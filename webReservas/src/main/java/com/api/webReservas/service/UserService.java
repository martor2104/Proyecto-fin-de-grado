package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;



public interface UserService {

	ResponseEntity<?> getAll(User loggedUser);
	ResponseEntity<?> getById(User loggedUser, Long id);
	ResponseEntity<?> deleteUser(User loggedUser, Long id);
	ResponseEntity<?>  putUser(User loggedUser, Long id, UserDTO user);
}
