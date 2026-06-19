package com.example.demo.service;

import com.example.demo.entity.Pokemon;
import com.example.demo.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<Pokemon> findAll() {
        return pokemonRepository.findAll();
    }

    public Optional<Pokemon> findById(Integer id) {
        return pokemonRepository.findById(id);
    }

    public Optional<Pokemon> findByUuid(String uuid) {
        return pokemonRepository.findByUuid(uuid);
    }

    public List<Pokemon> findByTipoUuid(String tipoUuid) {
        return pokemonRepository.findByTipoPokemon_Uuid(tipoUuid);
    }

    public Pokemon save(Pokemon pokemon) {
        if (pokemon.getUuid() == null || pokemon.getUuid().isBlank()) {
            pokemon.setUuid(UUID.randomUUID().toString());
        }
        return pokemonRepository.save(pokemon);
    }

    public void deleteById(Integer id) {
        pokemonRepository.deleteById(id);
    }

    public boolean deleteByUuid(String uuid) {
        return pokemonRepository.findByUuid(uuid)
            .map(pokemon -> {
                pokemonRepository.delete(pokemon);
                return true;
            }).orElse(false);
    }
}
