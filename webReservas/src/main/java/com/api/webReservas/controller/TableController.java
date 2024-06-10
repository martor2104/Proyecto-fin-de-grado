package com.api.webReservas.controller;

import com.api.webReservas.dto.TableDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve todos los usuarios")
    public ResponseEntity<?> getAllUsers() {
        return tableService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario", description = "Obtiene un usuario por id")
    public ResponseEntity<?> getUserById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return tableService.getById(id);
    }

    @PutMapping("/delete/{id}")
    @Operation(summary = "Borra un usuario", description = "Deshabilita un usuario de la base de datos por su id")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return tableService.deleteTable((User) userDetails, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un usuario", description = "Actualiza un usuario por su id")
    public ResponseEntity<?> putUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody TableDTO table) {
        return tableService.putTable((User) userDetails, id, table);
    }
}
