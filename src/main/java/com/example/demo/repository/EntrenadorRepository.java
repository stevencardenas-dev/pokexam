package com.example.demo.repository;

import com.example.demo.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Integer> {
    Optional<Entrenador> findByUuid(String uuid);
}

