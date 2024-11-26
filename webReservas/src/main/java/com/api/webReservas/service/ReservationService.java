package com.api.webReservas.service;

import org.springframework.http.ResponseEntity;

import com.api.webReservas.dto.ReservationDTO;
import com.api.webReservas.entity.Reservation;
import com.api.webReservas.entity.User;

public interface ReservationService {

	ResponseEntity<?> getAll();
	ResponseEntity<?> getById(Long id);
	ResponseEntity<?> saveReservation(User loggedUser, ReservationDTO Reservation);
	ResponseEntity<?> deleteReservation(User loggedUser, Long id);
	ResponseEntity<?> putReservation(User loggedUser, Long id, ReservationDTO Reservation);
    ResponseEntity<?> createReservation(User userDetails, ReservationDTO reservationDTO, Long mesaId);
}
