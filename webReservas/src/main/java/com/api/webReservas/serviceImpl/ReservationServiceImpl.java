package com.api.webReservas.serviceImpl;

import com.api.webReservas.entity.*;
import com.api.webReservas.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.ReservationDTO;
import com.api.webReservas.repository.ReservationRepository;
import com.api.webReservas.service.ReservationService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private ReservationRepository repository;

	@Autowired
	private TableRepository tableRepository;

	@Override
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll().stream().map(Reservation::toDTO));
	}

	@Override
	public ResponseEntity<?> getById(Long id) {
		Reservation reservation = repository.findById(id).orElse(null);
		
		if (reservation != null) {
			return ResponseEntity.status(HttpStatus.OK).body(Reservation.toDTO(reservation));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Reservation doesn't exist"));
		}
	}

	@Override
	public ResponseEntity<?> saveReservation(User loggedUser, ReservationDTO reservation) {
		// Obtener la fecha actual
		LocalDate today = LocalDate.now();

		// Validar que la fecha de la reserva no sea en el pasado
		if (reservation.getReservationDate().isBefore(today)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDTO("You cannot reserve a table for a date in the past"));
		}

		// Crear y guardar la nueva reserva
		Reservation newReservation = new Reservation(reservation);
		Reservation savedReservation = repository.save(newReservation);

		return ResponseEntity.status(HttpStatus.OK).body(Reservation.toDTO(savedReservation));
	}



	@Transactional
	public ResponseEntity<?> deleteReservation(User loggedUser, Long reservationId) {
		// Verificar que la reserva exista y pertenezca al usuario logueado
		Reservation reservation = repository.findById(reservationId).orElse(null);

		if (reservation != null && reservation.getUser().getId().equals(loggedUser.getId()) || loggedUser.getRole() == Role.ADMIN) {
			// Paso 1: Establecer la referencia de `reservation` en `null` en la tabla `tables`
			tableRepository.removeReservationReference(reservationId);

			// Paso 2: Eliminar la reserva en la tabla `reservations`
			repository.delete(reservation);
			return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Reservation deleted"));
		} else if (reservation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Reservation doesn't exist"));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You are not allowed to delete this reservation"));
		}
	}

	@Override
	public ResponseEntity<?> putReservation(User loggedUser, Long id, ReservationDTO reservation) {
		
		Reservation oldReservation = repository.findById(id).orElse(null);

		if (oldReservation != null) {

			oldReservation.setReservationDate((reservation.getReservationDate() != null)? reservation.getReservationDate() : oldReservation.getReservationDate());
			oldReservation.setUser((reservation.getUser() != null) ? reservation.getUser() : oldReservation.getUser());

			return ResponseEntity.status(HttpStatus.OK).body(Reservation.toDTO(repository.save(oldReservation)));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Reservation doesn't exist"));
		}

	}

	@Transactional
	@Override
	public ResponseEntity<?> createReservation(User user, ReservationDTO reservationDTO, Long mesaId) {
		// 1. Crear la reserva usando los datos del DTO
		Reservation nuevaReserva = new Reservation();
		nuevaReserva.setReservationDate(reservationDTO.getReservationDate());
		nuevaReserva.setUser(user); // Asociar la reserva al usuario autenticado

		// 2. Guardar la nueva reserva en la base de datos
		nuevaReserva = repository.save(nuevaReserva);

		// 3. Buscar la mesa por su ID
		Table mesa = tableRepository.findById(mesaId).orElse(null);
		if (mesa == null) {
			return ResponseEntity.badRequest().body("Mesa no encontrada");
		}

		// 4. Asociar la mesa a la reserva creada y cambiar el estado de la mesa
		mesa.setReservation(nuevaReserva);  // Asocia la reserva a la mesa
		mesa.setTableStatus(TableStatus.RESERVED);  // Cambiar el estado de la mesa a RESERVED

		// 5. Guardar los cambios en la mesa
		tableRepository.save(mesa);

		// 6. Devolver la reserva creada como respuesta
		return ResponseEntity.ok(Reservation.toDTO(nuevaReserva));
	}

}
