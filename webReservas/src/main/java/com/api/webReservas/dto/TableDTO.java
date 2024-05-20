package com.api.webReservas.dto;

import com.api.webReservas.entity.TableStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableDTO {
	
	private Long id;
	private String reservation;
	private TableStatus tableStatus;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReservation() {
		return reservation;
	}
	public void setReservation(String reservation) {
		this.reservation = reservation;
	}
	public TableStatus getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(TableStatus tableStatus) {
		this.tableStatus = tableStatus;
	}
	
	
}
