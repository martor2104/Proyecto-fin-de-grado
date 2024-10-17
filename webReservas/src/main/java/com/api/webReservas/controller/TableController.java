package com.api.webReservas.controller;

import com.api.webReservas.dto.TableDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Obtener todas las mesas", description = "Devuelve todos las mesas")
    public ResponseEntity<?> getAllTables() {
        return tableService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una mesa", description = "Obtiene una mesa por id")
    public ResponseEntity<?> getTableById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return tableService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Borra una mesa", description = "Deshabilita una mesa de la base de datos por su id")
    @SecurityRequirement(name = "adminAuth")
    public ResponseEntity<?> deleteTable(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return tableService.deleteTable((User) userDetails, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una mesa", description = "Actualiza una mesa por su id")
    @SecurityRequirement(name = "adminAuth")
    public ResponseEntity<?> putTable(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody TableDTO table) {
        return tableService.putTable((User) userDetails, id, table);
    }

    @PostMapping
    @Operation(summary = "Guarda una mesa", description = "Guarda una mesa en la base de datos")
    @SecurityRequirement(name = "adminAuth")
    public ResponseEntity<?> postTable(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TableDTO table){
        return  tableService.saveTable((User) userDetails, table);
    }
}
