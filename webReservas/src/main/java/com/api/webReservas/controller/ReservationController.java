package com.api.webReservas.controller;

import com.api.webReservas.dto.PlateDTO;
import com.api.webReservas.dto.ReservationDTO;
import com.api.webReservas.entity.User;
import com.api.webReservas.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/delete/{id}")
    @Operation(summary = "Borra una reserva", description = "Deshabilita una reserva de la base de datos por su id")
    public ResponseEntity<?> deleteReservcation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return reservationService.deleteReservation((User) userDetails, id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un plato", description = "Actualiza un plato por su id")
    public ResponseEntity<?> putReservation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody ReservationDTO reservation) {
        return reservationService.putReservation((User) userDetails, id, reservation);
    }
}
