package com.example.demo.controller;

import com.example.demo.entity.Pokemon;
import com.example.demo.entity.TipoPokemon;
import com.example.demo.service.EntrenadorService;
import com.example.demo.service.PokemonService;
import com.example.demo.service.TipoPokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class PublicApiController {

    private final EntrenadorService entrenadorService;
    private final PokemonService pokemonService;
    private final TipoPokemonService tipoPokemonService;

    @Autowired
    public PublicApiController(EntrenadorService entrenadorService,
                               PokemonService pokemonService,
                               TipoPokemonService tipoPokemonService) {
        this.entrenadorService = entrenadorService;
        this.pokemonService = pokemonService;
        this.tipoPokemonService = tipoPokemonService;
    }

    @PostMapping("/entrenador/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return entrenadorService.findByEmail(request.email())
                .map(entrenador -> ResponseEntity.ok(new LoginResponse(entrenador.getUuid())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/pokemons/{tipo}")
    public ResponseEntity<List<PokemonResponse>> findByTipo(@PathVariable String tipo) {
        List<Pokemon> pokemons = pokemonService.findByTipoUuid(tipo);
        return ResponseEntity.ok(pokemons.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @PostMapping("/pokemons")
    public ResponseEntity<PokemonResponse> createPokemon(@RequestBody PokemonCreateRequest request) {
        TipoPokemonRequest tipoPokemonRequest = request.tipoPokemon();
        TipoPokemon tipoPokemon = findTipoPokemon(tipoPokemonRequest);
        if (tipoPokemon == null) {
            return ResponseEntity.badRequest().build();
        }

        Pokemon pokemon = new Pokemon();
        pokemon.setNombre(request.nombre());
        pokemon.setDescripcion(request.descripcion());
        pokemon.setFechaDescubrimiento(request.fechaDescubrimiento());
        pokemon.setGeneracion(request.generacion());
        pokemon.setTipoPokemon(tipoPokemon);
        pokemon.setUuid(request.uuid() == null || request.uuid().isBlank()
                ? null : request.uuid());

        Pokemon saved = pokemonService.save(pokemon);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/entrenador/{uuid}/pokemons")
    public ResponseEntity<List<PokemonResponse>> getEntrenadorPokemons(@PathVariable("uuid") String entrenadorUuid) {
        List<PokemonResponse> response = entrenadorService.getPokemonsByUuid(entrenadorUuid).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/entrenador/{entrenadorUuid}/pokemons/{pokemonUuid}")
    public ResponseEntity<Void> addPokemonToEntrenador(@PathVariable String entrenadorUuid,
                                                       @PathVariable String pokemonUuid) {
        boolean added = entrenadorService.capturarPokemon(entrenadorUuid, pokemonUuid);
        if (added) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private TipoPokemon findTipoPokemon(TipoPokemonRequest request) {
        if (request == null) {
            return null;
        }
        if (request.uuid() != null && !request.uuid().isBlank()) {
            return tipoPokemonService.findByUuid(request.uuid()).orElse(null);
        }
        if (request.id() != null) {
            return tipoPokemonService.findById(request.id()).orElse(null);
        }
        return null;
    }

    private PokemonResponse toResponse(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.getId(),
                pokemon.getUuid(),
                pokemon.getNombre(),
                pokemon.getDescripcion(),
                pokemon.getFechaDescubrimiento(),
                pokemon.getGeneracion(),
                new TipoPokemonResponse(
                        pokemon.getTipoPokemon().getId(),
                        pokemon.getTipoPokemon().getUuid(),
                        pokemon.getTipoPokemon().getDescripcion())
        );
    }

    public record LoginRequest(String email) {
    }

    public record LoginResponse(String uuid) {
    }

    public record PokemonCreateRequest(
            String nombre,
            String descripcion,
            LocalDate fechaDescubrimiento,
            Integer generacion,
            TipoPokemonRequest tipoPokemon,
            String uuid) {
    }

    public record TipoPokemonRequest(Integer id, String uuid, String descripcion) {
    }

    public record PokemonResponse(
            Integer id,
            String uuid,
            String nombre,
            String descripcion,
            LocalDate fechaDescubrimiento,
            Integer generacion,
            TipoPokemonResponse tipoPokemon) {
    }

    public record TipoPokemonResponse(Integer id, String uuid, String descripcion) {
    }
}
