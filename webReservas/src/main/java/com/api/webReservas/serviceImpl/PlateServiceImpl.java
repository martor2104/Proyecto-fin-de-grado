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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;

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
	public ResponseEntity<?> savePlate(User loggedUser, PlateDTO plateDTO, MultipartFile image) {
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("No tienes permisos para guardar platos"));
		}

		Plate newPlate = new Plate();
		newPlate.setNamePlate(plateDTO.getNamePlate());
		newPlate.setDescription(plateDTO.getDescription());
		newPlate.setPrice(plateDTO.getPrice());
		newPlate.setCategory(plateDTO.getCategory());

		if (image != null && !image.isEmpty()) {
			String imageUrl = saveImageToDirectory(image);
			newPlate.setImg(imageUrl);
		}

		Plate savedPlate = repository.save(newPlate); // Aquí guardamos el plato en la base de datos

		if (savedPlate.getId() == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el plato");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(Plate.toDTO(savedPlate));
	}




	private String saveImageToDirectory(MultipartFile image) {
		try {
			String directoryPath = "E:/TFG/Front/Proyecto-fin-de-grado/FrontReservas/webReservas/src/assets/img_plates";
			String filePath = Paths.get(directoryPath, image.getOriginalFilename()).toString();
			Files.copy(image.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
			return "assets/img_plates/" + image.getOriginalFilename(); // Devuelve la URL relativa
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ResponseEntity<?> putPlate(User loggedUser, Long id, PlateDTO plate, MultipartFile image) {
		if (!loggedUser.getRole().equals(Role.ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorDTO("You don't have permissions to modify plate"));
		}

		Plate oldPlate = repository.findById(id).orElse(null);

		if (oldPlate == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorDTO("Plate doesn't exist"));
		}

		// Actualizar campos solo si los valores no son nulos
		oldPlate.setNamePlate(Optional.ofNullable(plate.getNamePlate()).orElse(oldPlate.getNamePlate()));
		oldPlate.setDescription(Optional.ofNullable(plate.getDescription()).orElse(oldPlate.getDescription()));
		oldPlate.setPrice(Optional.ofNullable(plate.getPrice()).orElse(oldPlate.getPrice()));

		// Guardar la nueva imagen si se proporciona
		if (image != null && !image.isEmpty()) {
			String imageUrl = saveImageToDirectory(image);
			oldPlate.setImg(imageUrl); // Actualizar la URL de la imagen
		}

		// Guardar los cambios y devolver el DTO actualizado
		Plate savedPlate = repository.save(oldPlate);
		return ResponseEntity.ok(Plate.toDTO(savedPlate));
	}

	@Override
	public ResponseEntity<?> deletePlate(Long id, UserDetails userDetails) {
		if (userDetails == null) {
			System.out.println("Usuario no autenticado");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Usuario no autenticado"));
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
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("error", "No tienes permisos para eliminar este plato"));
		}

		Plate plate = repository.findById(id).orElse(null);
		if (plate == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", "Plato no encontrado"));
		}

		repository.delete(plate);

		// Respuesta JSON válida
		return ResponseEntity.ok(Map.of("message", "Plato eliminado"));
	}


}
