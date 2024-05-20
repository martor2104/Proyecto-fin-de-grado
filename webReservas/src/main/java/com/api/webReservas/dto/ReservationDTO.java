package com.api.webReservas.dto;

import java.time.LocalDate;

import com.api.webReservas.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDTO {

	private Long id;
	private User user;
	private LocalDate reservationDate;
	
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
