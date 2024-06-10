package com.api.webReservas.entity;

import java.time.LocalDate;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.ReservationDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "reservations")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "reservation_date", nullable = false)
	private LocalDate reservationDate;
	
	public Reservation(ReservationDTO reservation) {
		this.id = reservation.getId();
		this.user = reservation.getUser();
		this.reservationDate = reservation.getReservationDate();
	}

	public static ReservationDTO toDTO(Reservation reservation) {
		if (reservation == null) {
			return null;
		}
		return ReservationDTO.builder()
				.id(reservation.getId())
				.reservationDate(reservation.getReservationDate())
				.user(reservation.getUser())
				.build();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}
	
	
}
