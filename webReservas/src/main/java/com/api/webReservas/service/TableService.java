package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.dto.TableDTO;
import com.api.webReservas.entity.Table;
import com.api.webReservas.entity.User;

public interface TableService {

	ResponseEntity<?> getAll();
	ResponseEntity<?> getById(Long id);
	ResponseEntity<?> deleteTable(User loggedUser, Long id);
	ResponseEntity<?> putTable(User loggedUser, Long id, TableDTO table);
	ResponseEntity<?> saveTable(User loggedUser, TableDTO table);
	
}
