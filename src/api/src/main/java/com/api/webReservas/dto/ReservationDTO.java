package com.api.webReservas.dto;

import java.time.LocalDate;
import com.api.webReservas.entity.User;

public class ReservationDTO {

	private Long id;
	private User user;
	private LocalDate reservationDate;

	// Constructor vacío
	public ReservationDTO() {
	}

	// Constructor con todos los campos
	public ReservationDTO(Long id, User user, LocalDate reservationDate) {
		this.id = id;
		this.user = user;
		this.reservationDate = reservationDate;
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
		return "ReservationDTO{" +
				"id=" + id +
				", user=" + user +
				", reservationDate=" + reservationDate +
				'}';
	}
}
