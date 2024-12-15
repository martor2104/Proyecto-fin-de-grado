package com.api.webReservas.serviceImpl;

import com.api.webReservas.dto.ErrorDTO;
import com.api.webReservas.dto.MessageDTO;
import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.entity.Plate;
import com.api.webReservas.entity.Role;
import com.api.webReservas.entity.User;
import com.api.webReservas.repository.PlateRepository;
import com.api.webReservas.repository.UserRepository;
import com.api.webReservas.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;

@Service
public class PlateServiceImpl implements PlateService {

	@Autowired
	private PlateRepository repository;

	@Autowired
	private UserRepository userRepository;

	private final Path baseDirectory = Paths.get(System.getProperty("user.dir"), "src", "Images", "img_plates").normalize();

	@Override
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll().stream().map(Plate::toDTO));
	}

	@Override
	public ResponseEntity<?> getById(Long id) {
		Plate plate = repository.findById(id).orElse(null);

		if (plate != null) {
			return ResponseEntity.status(HttpStatus.OK).body(Plate.toDTO(plate));
		} else {
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

		Plate savedPlate = repository.save(newPlate);

		if (savedPlate.getId() == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el plato");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(Plate.toDTO(savedPlate));
	}

	private String saveImageToDirectory(MultipartFile image) {
		try {
			// Ruta relativa para guardar las imágenes en src/Images/assets/img_plates
			Path baseDirectory = Paths.get(System.getProperty("user.dir"), "..", "FrontReservas","webReservas", "src", "Images", "assets", "img_plates").normalize();

			// Crear el directorio si no existe
			if (!Files.exists(baseDirectory)) {
				Files.createDirectories(baseDirectory);
			}

			// Validar que el archivo no sea nulo ni vacío
			String originalFilename = image.getOriginalFilename();
			if (originalFilename == null || originalFilename.trim().isEmpty()) {
				throw new IllegalArgumentException("El archivo de imagen no tiene un nombre válido.");
			}
			if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
				throw new IllegalArgumentException("El nombre del archivo contiene caracteres inseguros.");
			}

			// Construir la ruta del archivo
			Path filePath = baseDirectory.resolve(originalFilename);

			// Guardar el archivo en el sistema
			Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			// Log de éxito
			System.out.println("Archivo guardado correctamente en: " + filePath.toAbsolutePath());

			// Devolver la URL relativa para el frontend
			return "assets/img_plates/" + originalFilename;
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

		oldPlate.setNamePlate(Optional.ofNullable(plate.getNamePlate()).orElse(oldPlate.getNamePlate()));
		oldPlate.setDescription(Optional.ofNullable(plate.getDescription()).orElse(oldPlate.getDescription()));
		oldPlate.setPrice(Optional.ofNullable(plate.getPrice()).orElse(oldPlate.getPrice()));

		if (image != null && !image.isEmpty()) {
			String imageUrl = saveImageToDirectory(image);
			oldPlate.setImg(imageUrl);
		}

		Plate savedPlate = repository.save(oldPlate);
		return ResponseEntity.ok(Plate.toDTO(savedPlate));
	}

	@Override
	public ResponseEntity<?> deletePlate(Long id, UserDetails userDetails) {
		if (userDetails == null || userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No tienes permisos para eliminar este plato"));
		}

		Plate plate = repository.findById(id).orElse(null);
		if (plate == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Plato no encontrado"));
		}

		repository.delete(plate);
		return ResponseEntity.ok(Map.of("message", "Plato eliminado"));
	}
}
