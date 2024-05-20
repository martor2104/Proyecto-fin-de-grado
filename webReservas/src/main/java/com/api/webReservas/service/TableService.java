package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.entity.Table;
import com.api.webReservas.entity.User;

public interface TableService {

	ResponseEntity<?> getAll(User loggedUser);
	ResponseEntity<?> getById(User loggedUser, Long id);
	ResponseEntity<?> saveTable(User loggedUser, Table table);
	ResponseEntity<?> deleteTable(User loggedUser, Long id);
	ResponseEntity<?> putTable(User loggedUser, Long id, Table table);
	
}
