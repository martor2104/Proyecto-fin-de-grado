package com.api.webReservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.webReservas.entity.Table;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long>{

    void deleteByReservationId(Long reservationId);

    @Modifying
    @Transactional
    @Query("UPDATE tables t SET t.reservation = NULL WHERE t.reservation.id = :reservationId")
    void removeReservationReference(Long reservationId);

    @Query("SELECT MAX(t.numeroMesa) FROM tables t")
    Integer findMaxNumeroMesa();

    Optional<Table> findByNumeroMesa(int numeroMesa);

    Table findByReservationId(Long reservationId);

    boolean existsByNumeroMesa(int numeroMesa);

}
