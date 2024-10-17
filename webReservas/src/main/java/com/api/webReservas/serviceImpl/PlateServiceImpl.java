package com.api.webReservas.serviceImpl;

import com.api.webReservas.entity.Table;
import com.api.webReservas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

	@Autowired
	private UserRepository userRepository;

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
			Plate savedPlate = repository.save(newPlate);
			return ResponseEntity.status(HttpStatus.CREATED).body(Plate.toDTO(savedPlate));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("You doesn't have permissions to save tables"));
		}
    }


	@Override
	public ResponseEntity<?> deletePlate(Long id, UserDetails userDetails) {
		if (userDetails == null) {
			System.out.println("Usuario no autenticado");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
		}

		userDetails.getAuthorities().forEach(authority -> {
			System.out.println("GrantedAuthority: " + authority.getAuthority());
		});

		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

		System.out.println("Usuario autenticado: " + userDetails.getUsername());
		System.out.println("Roles del usuario: " + userDetails.getAuthorities());
		System.out.println("¿El usuario tiene rol de ADMIN?: " + isAdmin);

		if (!isAdmin) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para eliminar este plato");
		}

		Plate plate = repository.findById(id).orElse(null);
		if (plate == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plato no encontrado");
		}

		repository.delete(plate);
		return ResponseEntity.status(HttpStatus.OK).body("Plato eliminado");
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
