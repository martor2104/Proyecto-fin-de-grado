package com.api.webReservas.serviceImpl;

import com.api.webReservas.entity.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.entity.Plate;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.PlateRepository;
import com.api.webReservas.service.PlateService;

@Service
public class PlateServiceImpl implements PlateService{
	
	@Autowired
	private PlateRepository repository;

	@Override
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll().stream().map(Plate::toDTO));
	}

	@Override
	public ResponseEntity<?> getById(Long id) {
		
		Plate plate = repository.findById(id).orElse(null);
		
		if (plate != null) {
			return ResponseEntity.status(HttpStatus.OK).body(Plate.toDTO(plate));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Plate doesn't exist"));
		}
	}

	@Override
	public ResponseEntity<?> savePlate(User loggedUser, PlateDTO plate) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			Plate newPlate = new Plate(plate);
			return ResponseEntity.status(HttpStatus.OK).body(Plate.toDTO(repository.save(newPlate)));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to save tables"));
		}
    }
	

	@Override
	public ResponseEntity<?> deletePlate(User loggedUser, Long id) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			Plate plate = repository.findById(id).orElse(null);

			if(plate != null) {
				repository.delete(plate);
				return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Plate deleted"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Plate doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to delete plates"));
		}
	}

	@Override
	public ResponseEntity<?> putPlate(User loggedUser, Long id, PlateDTO plate) {
		if (loggedUser.getRole().equals(Role.ADMIN)) {
			Plate oldPlate = repository.findById(id).orElse(null);

			if (oldPlate != null) {

				oldPlate.setNamePlate((plate.getNamePlate() != null) ? plate.getNamePlate() : oldPlate.getNamePlate());
				oldPlate.setDescription((plate.getDescription() != null)? plate.getDescription() : oldPlate.getDescription());
				oldPlate.setPrice((plate.getPrice() != null) ? plate.getPrice() : oldPlate.getPrice());

				return ResponseEntity.status(HttpStatus.OK).body(Plate.toDTO(repository.save(oldPlate)));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("User doesn't exist"));
			}

		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to modify user"));
		}
	}

}
