package com.example.demo.repository;

import com.example.demo.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {
    Optional<Pokemon> findByUuid(String uuid);
    List<Pokemon> findByTipoPokemon_Uuid(String uuid);
}
