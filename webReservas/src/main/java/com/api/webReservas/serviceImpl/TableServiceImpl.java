package com.api.webReservas.serviceImpl;

import com.api.webReservas.entity.TableStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.TableDTO;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.Table;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.TableRepository;
import com.api.webReservas.service.TableService;
import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {

	@Autowired
	private TableRepository repository;

	@Override
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll().stream().map(Table::toDTO));
	}

	@Override
	public ResponseEntity<?> getById(Long id) {
		Table table = repository.findById(id).orElse(null);

		if (table != null) {
			return ResponseEntity.status(HttpStatus.OK).body(Table.toDTO(table));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Table doesn't exist"));
		}
	}

	@Override
	public ResponseEntity<?> deleteTable(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			Table table = repository.findById(id).orElse(null);

			if (table != null) {
				repository.delete(table);
				return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Table deleted"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Table doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You don't have permissions to delete tables"));
		}
	}

	@Override
	public ResponseEntity<?> putTable(User loggedUser, Long id, TableDTO tableDTO) {
		// Verificar si el usuario es ADMIN
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("No tienes permisos para modificar la mesa"));
		}

		// Obtener la mesa actual
		Table existingTable = repository.findById(id).orElse(null);

		// Si la mesa no existe
		if (existingTable == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("La mesa no existe"));
		}

		// Actualizar los campos de la mesa
		existingTable.setReservation((tableDTO.getReservation() != null) ? tableDTO.getReservation() : existingTable.getReservation());

		// Verificar si se ha actualizado la reserva y cambiar el estado de la mesa en consecuencia
		if (existingTable.getReservation() != null) {
			existingTable.setTableStatus(TableStatus.RESERVED);  // Cambiar a RESERVED si la reserva no es null
		} else {
			existingTable.setTableStatus(TableStatus.PENDING);  // Mantener PENDING si la reserva es null
		}

		// Guardar la mesa actualizada
		Table updatedTable = repository.save(existingTable);

		// Devolver la mesa actualizada en el cuerpo de la respuesta
		return ResponseEntity.status(HttpStatus.OK).body(Table.toDTO(updatedTable));
	}

	@Override
	public ResponseEntity<?> addTable(User loggedUser, TableDTO tableDTO) {
		// Verificar si el usuario tiene permisos
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para añadir una mesa.");
		}

		// Validar que el número de mesa no esté duplicado
		if (repository.existsByNumeroMesa(tableDTO.getNumeroMesa())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El número de mesa ya está en uso.");
		}

		// Crear la nueva mesa
		Table newTable = new Table();
		newTable.setReservation(null);  // No hay reserva al crear la mesa
		newTable.setTableStatus(TableStatus.PENDING);  // Estado inicial es PENDING
		newTable.setNumeroMesa(tableDTO.getNumeroMesa());

		repository.save(newTable);
		return ResponseEntity.status(HttpStatus.CREATED).body(Table.toDTO(newTable));
	}

	public ResponseEntity<?> getMesaByNumero(int numeroMesa) {
		Optional<Table> table = repository.findByNumeroMesa(numeroMesa);
		if (table.isPresent()) {
			return ResponseEntity.ok(Table.toDTO(table.get()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mesa no encontrada");
		}
	}
}
