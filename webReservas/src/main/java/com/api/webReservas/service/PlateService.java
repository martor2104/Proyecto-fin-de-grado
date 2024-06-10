package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.entity.User;

public interface PlateService {

	ResponseEntity<?> getAll();
	ResponseEntity<?> getById(Long id);
	ResponseEntity<?> savePlate(User loggedUser, PlateDTO Plate);
	ResponseEntity<?> deletePlate(User loggedUser, Long id);
	ResponseEntity<?> putPlate(User loggedUser, Long id, PlateDTO Plate);
	
}
