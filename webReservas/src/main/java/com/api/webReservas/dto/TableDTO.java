package com.api.webReservas.dto;

import com.api.webReservas.entity.Reservation;
import com.api.webReservas.entity.TableStatus;

public class TableDTO {

	private Long id;
	private Reservation reservation;
	private TableStatus tableStatus;
	private int numeroMesa;

	// Constructor con todos los campos
	public TableDTO(Long id, Reservation reservation, TableStatus tableStatus, int numeroMesa) {
		this.id = id;
		this.reservation = reservation;
		this.tableStatus = tableStatus;
		this.numeroMesa = numeroMesa;
	}

	// Constructor vacío
	public TableDTO() {
	}

	// Getters y Setters

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

	@Override
	public String toString() {
		return "TableDTO{" +
				"id=" + id +
				", reservation=" + reservation +
				", tableStatus=" + tableStatus +
				", numeroMesa=" + numeroMesa +
				'}';
	}
}
