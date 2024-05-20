package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.entity.Reservation;
import com.api.webReservas.entity.User;

public interface ReservationService {

	ResponseEntity<?> getAll(User loggedUser);
	ResponseEntity<?> getById(User loggedUser, Long id);
	ResponseEntity<?> saveReservation(User loggedUser, Reservation Reservation);
	ResponseEntity<?> deleteReservation(User loggedUser, Long id);
	ResponseEntity<?> putReservation(User loggedUser, Long id, Reservation Reservation);
	
}
