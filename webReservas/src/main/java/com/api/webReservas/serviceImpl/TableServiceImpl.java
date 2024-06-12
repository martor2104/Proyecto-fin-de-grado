package com.api.webReservas.serviceImpl;

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
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Table doesn't exist"));
		}
	}

	@Override
	public ResponseEntity<?> saveTable(User loggedUser, TableDTO table) {
		
        if (loggedUser.getRole().equals(Role.ADMIN)) {
            Table newTable = new Table(table);
            return ResponseEntity.status(HttpStatus.OK).body(Table.toDTO(repository.save(newTable)));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to save tables"));
        }
	}

	@Override
	public ResponseEntity<?> deleteTable(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			Table table = repository.findById(id).orElse(null);

			if(table != null) {
				repository.delete(table);
				return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Table deleted"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Table doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to delete tables"));
		}
	}

	@Override
	public ResponseEntity<?> putTable(User loggedUser, Long id, TableDTO table) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			Table oldTable = repository.findById(id).orElse(null);

			if (oldTable != null) {

				oldTable.setReservation((table.getReservation() != null)? table.getReservation() : oldTable.getReservation());
				oldTable.setTableStatus((table.getTableStatus() != null)? table.getTableStatus() : oldTable.getTableStatus());

				return ResponseEntity.status(HttpStatus.OK).body(Table.toDTO(repository.save(oldTable)));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Table doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to modify table"));
		}
	}

}
