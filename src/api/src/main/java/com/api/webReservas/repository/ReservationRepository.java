package com.api.webReservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.webReservas.entity.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    List<Reservation> findByUserId(Long userId);
}
