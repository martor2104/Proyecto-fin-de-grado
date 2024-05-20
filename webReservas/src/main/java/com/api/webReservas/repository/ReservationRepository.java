package com.api.webReservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.webReservas.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
