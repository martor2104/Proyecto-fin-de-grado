package com.api.webReservas.entity;

import java.time.LocalDate;
import com.api.webReservas.dto.ReservationDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "reservations")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "reservation_date", nullable = false)
	private LocalDate reservationDate;

	// Constructor vacío
	public Reservation() {
	}

	// Constructor con todos los campos
	public Reservation(Long id, User user, LocalDate reservationDate) {
		this.id = id;
		this.user = user;
		this.reservationDate = reservationDate;
	}

	// Constructor que toma un DTO como parámetro
	public Reservation(ReservationDTO reservation) {
		this.id = reservation.getId();
		this.user = reservation.getUser();
		this.reservationDate = reservation.getReservationDate();
	}

	// Metodo estático que convierte un Reservation en un ReservationDTO
	public static ReservationDTO toDTO(Reservation reservation) {
		if (reservation == null) {
			return null;
		}
		return new ReservationDTO(
				reservation.getId(),
				reservation.getUser(),
				reservation.getReservationDate()
		);
	}

	// Getters y Setters

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

	@Override
	public String toString() {
		return "Reservation{" +
				"id=" + id +
				", user=" + user +
				", reservationDate=" + reservationDate +
				'}';
	}
}
