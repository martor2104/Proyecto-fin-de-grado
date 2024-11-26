package com.api.webReservas.controller;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.PlateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> putPlate(
            @RequestPart(value = "plate", required = false) String plateJson,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails loggedUser) {

        // Verificar que el usuario esté autenticado
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        // Deserializar el JSON del plato a PlateDTO
        ObjectMapper mapper = new ObjectMapper();
        PlateDTO plateDTO;
        try {
            plateDTO = mapper.readValue(plateJson, PlateDTO.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al deserializar el JSON del plato: " + e.getMessage());
        }

        // Llamar al servicio para actualizar el plato
        return plateService.putPlate((User) loggedUser, id, plateDTO, image);
    }






    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> savePlate(
            @RequestPart(value = "plate", required = false) String plateJson,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails loggedUser) {

        // Verificar que el usuario esté autenticado
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        // Deserializar el JSON del plato a PlateDTO
        ObjectMapper mapper = new ObjectMapper();
        PlateDTO plateDTO;
        try {
            plateDTO = mapper.readValue(plateJson, PlateDTO.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al deserializar el JSON del plato: " + e.getMessage());
        }

        // Llamar al servicio para guardar el plato
        return plateService.savePlate((User) loggedUser, plateDTO, image);
    }






}
