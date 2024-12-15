package com.api.webReservas.repository;

import com.api.webReservas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.webReservas.entity.Plate;

@Repository
public interface PlateRepository extends JpaRepository<Plate, Long>{

}
