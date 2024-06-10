package com.api.webReservas.controller;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.UserDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.PlateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/delete/{id}")
    @Operation(summary = "Borra un plato", description = "Deshabilita un plato de la base de datos por su id")
    public ResponseEntity<?> deletePlate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return plateService.deletePlate((User) userDetails, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un plato", description = "Actualiza un plato por su id")
    public ResponseEntity<?> putPlate(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody PlateDTO plate) {
        return plateService.putPlate((User) userDetails, id, plate);
    }
}
