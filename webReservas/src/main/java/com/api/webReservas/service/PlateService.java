package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface PlateService {

	ResponseEntity<?> getAll();
	ResponseEntity<?> getById(Long id);
	ResponseEntity<?> savePlate(User loggedUser, PlateDTO Plate);
	ResponseEntity<?> deletePlate(Long id, UserDetails userDetails);
	ResponseEntity<?> putPlate(User loggedUser, Long id, PlateDTO Plate);
	
}
