package com.api.webReservas.entity;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.TableDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tables")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "reserva_id")
	private Reservation reservation;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TableStatus tableStatus;

	public Table(TableDTO table) {
		this.id = table.getId();
		this.reservation = table.getReservation();
	}

	public static TableDTO toDTO(Table table) {
        if (table == null) {
            return null;
        }
        return new TableDTO(
        		table.getId(),
        		table.getReservation(),
        		table.getTableStatus()
			);
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
	
	
}
