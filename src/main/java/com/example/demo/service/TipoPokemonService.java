package com.example.demo.service;

import com.example.demo.entity.TipoPokemon;
import com.example.demo.repository.TipoPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TipoPokemonService {

    private final TipoPokemonRepository tipoPokemonRepository;

    @Autowired
    public TipoPokemonService(TipoPokemonRepository tipoPokemonRepository) {
        this.tipoPokemonRepository = tipoPokemonRepository;
    }

    public List<TipoPokemon> findAll() {
        return tipoPokemonRepository.findAll();
    }

    public Optional<TipoPokemon> findById(Integer id) {
        return tipoPokemonRepository.findById(id);
    }

    public Optional<TipoPokemon> findByUuid(String uuid) {
        return tipoPokemonRepository.findByUuid(uuid);
    }

    public TipoPokemon save(TipoPokemon tipoPokemon) {
        if (tipoPokemon.getUuid() == null || tipoPokemon.getUuid().isBlank()) {
            tipoPokemon.setUuid(UUID.randomUUID().toString());
        }
        return tipoPokemonRepository.save(tipoPokemon);
    }

    public void deleteById(Integer id) {
        tipoPokemonRepository.deleteById(id);
    }

    public boolean deleteByUuid(String uuid) {
        return tipoPokemonRepository.findByUuid(uuid)
            .map(tipoPokemon -> {
                tipoPokemonRepository.delete(tipoPokemon);
                return true;
            }).orElse(false);
    }
}
