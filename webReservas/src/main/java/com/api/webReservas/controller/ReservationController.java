package com.api.webReservas.controller;

import com.api.webReservas.dto.ReservationDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping()
    @Operation(summary = "Obtener todas las reservas", description = "Devuelve todas las reservas")
    public ResponseEntity<?> getAllReservations(@AuthenticationPrincipal UserDetails userDetails) {
        return reservationService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reserva", description = "Obtiene una reserva por id")
    public ResponseEntity<?> getReservationById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return reservationService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra una reserva", description = "Deshabilita una reserva de la base de datos por su id")
    @SecurityRequirement(name = "adminAuth")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteReservation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return reservationService.deleteReservation((User) userDetails, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un plato", description = "Actualiza un plato por su id")
    @SecurityRequirement(name = "adminAuth")
    public ResponseEntity<?> putReservation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody ReservationDTO reservation) {
        return reservationService.putReservation((User) userDetails, id, reservation);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva y asocia una mesa")
    public ResponseEntity<?> createReservation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReservationDTO reservationDTO,
            @RequestParam Long mesaId) {
        return reservationService.createReservation((User) userDetails, reservationDTO, mesaId);
    }

}
