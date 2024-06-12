package com.api.webReservas.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.ReservationDTO;
import com.api.webReservas.entity.Reservation;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.ReservationRepository;
import com.api.webReservas.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private ReservationRepository repository;

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
            Reservation newReservation = new Reservation(reservation);
            return ResponseEntity.status(HttpStatus.OK).body(Reservation.toDTO(repository.save(newReservation)));
        
	}

	@Override
	public ResponseEntity<?> deleteReservation(User loggedUser, Long id) {
		Reservation reservation = repository.findById(id).orElse(null);

		if(reservation != null && reservation.getUser().getId().equals(loggedUser.getId())) {
			repository.delete(reservation);
			return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Reservation deleted"));
		} else if (reservation == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Reservation doesn't exist"));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("You are not allowed to delete this reservation"));
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

}
