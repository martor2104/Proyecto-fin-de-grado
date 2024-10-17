package com.api.webReservas.controller;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.PlateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plates")
public class PlateController {
    @Autowired
    private PlateService plateService;

    @GetMapping()
    @Operation(summary = "Obtener todos los platos", description = "Devuelve todos los platos")
    public ResponseEntity<?> getAllPlates() {
        return plateService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un plato", description = "Obtiene un plato por id")
    public ResponseEntity<?> getPlateById(@PathVariable Long id) {
        return plateService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un plato", description = "Deshabilita un plato de la base de datos por su id")
    @SecurityRequirement(name = "adminAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePlate(@AuthenticationPrincipal User user, @PathVariable Long id) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
        return plateService.deletePlate(id, user);  // Pasa el objeto `User` correctamente
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un plato", description = "Actualiza un plato por su id")
    @SecurityRequirement(name = "adminAuth")
    public ResponseEntity<?> putPlate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody PlateDTO plate) {
        return plateService.putPlate((User) userDetails, id, plate);
    }

    @PostMapping()
    @Operation(summary = "Añadir un plato", description = "Añade un nuevo plato a la base de datos")
    public ResponseEntity<?> addPlate(@AuthenticationPrincipal UserDetails loggedUser, @RequestBody PlateDTO plateDTO) {
        return plateService.savePlate((User) loggedUser, plateDTO);
    }
}
