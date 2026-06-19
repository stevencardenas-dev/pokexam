package com.example.demo.repository;

import com.example.demo.entity.Captura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CapturaRepository extends JpaRepository<Captura, Integer> {
    List<Captura> findByEntrenador_Uuid(String uuid);
}
