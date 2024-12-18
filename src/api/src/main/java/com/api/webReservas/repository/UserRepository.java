package com.api.webReservas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.webReservas.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    @Query("SELECT u FROM users u WHERE u.name = :name")
    Optional<User> findByOptionalName(String name);

    User findByName(String name);

    boolean existsByEmail(String email);

}
