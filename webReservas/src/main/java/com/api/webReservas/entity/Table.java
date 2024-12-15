package com.api.webReservas.entity;

import com.api.webReservas.dto.TableDTO;
import jakarta.persistence.*;

@Entity(name = "tables")
public class Table {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "reserva_id")
	private Reservation reservation;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TableStatus tableStatus;

	@Column(nullable = false, unique = true)
	private int numeroMesa;

	// Constructor vacío
	public Table() {
	}

	// Constructor con todos los campos
	public Table(Long id, Reservation reservation, TableStatus tableStatus, int numeroMesa) {
		this.id = id;
		this.reservation = reservation;
		this.tableStatus = tableStatus;
		this.numeroMesa = numeroMesa;
	}

	// Constructor que toma un DTO como parámetro
	public Table(TableDTO table) {
		this.id = table.getId();
		this.reservation = table.getReservation();
		this.tableStatus = table.getTableStatus();
		this.numeroMesa = table.getNumeroMesa();
	}

	// Metodo estático que convierte un Table en un TableDTO
	public static TableDTO toDTO(Table table) {
		if (table == null) {
			return null;
		}
		return new TableDTO(
				table.getId(),
				table.getReservation(),
				table.getTableStatus(),
				table.getNumeroMesa()
		);
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
		return "Table{" +
				"id=" + id +
				", reservation=" + reservation +
				", tableStatus=" + tableStatus +
				", numeroMesa=" + numeroMesa +
				'}';
	}
}
