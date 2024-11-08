package com.api.webReservas.dto;

import com.api.webReservas.entity.Reservation;
import com.api.webReservas.entity.TableStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableDTO {
	
	private Long id;
	private Reservation reservation;
	private TableStatus tableStatus;
	private int numeroMesa;
	
	
	public TableDTO(Long id, Reservation reservation, TableStatus tableStatus, int numeroMesa) {
		this.id=id;
		this.reservation = reservation;
		this.tableStatus = tableStatus;
		this.numeroMesa = numeroMesa;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Reservation getReservation() {
		return reservation;
	}
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	public TableStatus getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(TableStatus tableStatus) {
		this.tableStatus = tableStatus;
	}

	public int getNumeroMesa() {
		return numeroMesa;
	}

	public void setNumeroMesa(int numeroMesa) {
		this.numeroMesa = numeroMesa;
	}
}
