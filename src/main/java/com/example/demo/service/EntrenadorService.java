package com.example.demo.service;

import com.example.demo.entity.Entrenador;
import com.example.demo.entity.Pokemon;
import com.example.demo.repository.EntrenadorRepository;
import com.example.demo.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;
    private final PokemonRepository pokemonRepository;
    private final com.example.demo.repository.CapturaRepository capturaRepository;

    @Autowired
    public EntrenadorService(EntrenadorRepository entrenadorRepository, PokemonRepository pokemonRepository, com.example.demo.repository.CapturaRepository capturaRepository) {
        this.entrenadorRepository = entrenadorRepository;
        this.pokemonRepository = pokemonRepository;
        this.capturaRepository = capturaRepository;
    }

    public List<Entrenador> findAll() {
        return entrenadorRepository.findAll();
    }

    public Optional<Entrenador> findById(Integer id) {
        return entrenadorRepository.findById(id);
    }

    public Optional<Entrenador> findByUuid(String uuid) {
        return entrenadorRepository.findByUuid(uuid);
    }

    /** Login: find entrenador by nombre + apellido */
    public Optional<Entrenador> login(String nombre, String apellido) {
        return entrenadorRepository.findAll().stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombre)
                          && e.getApellido().equalsIgnoreCase(apellido))
                .findFirst();
    }

    public Set<Pokemon> getPokemonsByUuid(String uuid) {
        List<com.example.demo.entity.Captura> capturas = capturaRepository.findByEntrenador_Uuid(uuid);
        Set<Pokemon> result = new HashSet<>();
        for (com.example.demo.entity.Captura c : capturas) {
            if (c.getPokemon() != null) result.add(c.getPokemon());
        }
        return result;
    }

    public Entrenador save(Entrenador entrenador) {
        if (entrenador.getUuid() == null || entrenador.getUuid().isBlank()) {
            entrenador.setUuid(UUID.randomUUID().toString());
        }
        return entrenadorRepository.save(entrenador);
    }

    public void deleteById(Integer id) {
        entrenadorRepository.deleteById(id);
    }

    public boolean deleteByUuid(String uuid) {
        return entrenadorRepository.findByUuid(uuid)
            .map(entrenador -> {
                entrenadorRepository.delete(entrenador);
                return true;
            }).orElse(false);
    }

    /** Links a Pokemon to an Entrenador (captura). */
    public boolean capturarPokemon(String entrenadorUuid, String pokemonUuid) {
        Optional<Entrenador> entrenadorOpt = entrenadorRepository.findByUuid(entrenadorUuid);
        Optional<Pokemon> pokemonOpt = pokemonRepository.findByUuid(pokemonUuid);

        if (entrenadorOpt.isPresent() && pokemonOpt.isPresent()) {
            Entrenador entrenador = entrenadorOpt.get();
            Pokemon pokemon = pokemonOpt.get();
            com.example.demo.entity.Captura captura = com.example.demo.entity.Captura.builder()
                    .entrenador(entrenador)
                    .pokemon(pokemon)
                    .fechaCaptura(java.time.LocalDate.now())
                    .uuid(java.util.UUID.randomUUID().toString())
                    .build();

            capturaRepository.save(captura);
            return true;
        }
        return false;
    }
}
