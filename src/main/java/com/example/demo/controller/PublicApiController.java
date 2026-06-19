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

    /**
     * POST /entrenador/login
     * Body: { "nombre": "Ash", "apellido": "Ketchum" }
     * Returns the trainer's UUID if found.
     */
    @PostMapping("/entrenador/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return entrenadorService.login(request.nombre(), request.apellido())
                .map(entrenador -> ResponseEntity.ok(new LoginResponse(entrenador.getUuid())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * GET /pokemons/{tipo}
     * {tipo} = UUID of the tipo_pokemon
     */
    @GetMapping("/pokemons/{tipo}")
    public ResponseEntity<List<PokemonResponse>> findByTipo(@PathVariable String tipo) {
        List<Pokemon> pokemons = pokemonService.findByTipoUuid(tipo);
        return ResponseEntity.ok(pokemons.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    /**
     * POST /pokemons
     * Body: { "nombre": "Pikachu", "descripcion": "...", "generacion": 1,
     *         "fechaDescubrimiento": "1996-02-27",
     *         "tipoPokemonId": 1 }
     * tipoPokemonId = numeric ID of the tipo_pokemon row
     */
    @PostMapping("/pokemons")
    public ResponseEntity<PokemonResponse> createPokemon(@RequestBody PokemonCreateRequest request) {
        TipoPokemon tipoPokemon = tipoPokemonService.findById(request.tipoPokemonId()).orElse(null);
        if (tipoPokemon == null) {
            return ResponseEntity.badRequest().build();
        }

        Pokemon pokemon = new Pokemon();
        pokemon.setNombre(request.nombre());
        pokemon.setDescripcion(request.descripcion());
        pokemon.setFechaDescubrimiento(request.fechaDescubrimiento());
        pokemon.setGeneracion(request.generacion());
        pokemon.setTipoPokemon(tipoPokemon);
        // uuid is auto-generated if not provided
        if (request.uuid() != null && !request.uuid().isBlank()) {
            pokemon.setUuid(request.uuid());
        }

        Pokemon saved = pokemonService.save(pokemon);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    /**
     * GET /entrenador/{uuid}/pokemons
     */
    @GetMapping("/entrenador/{uuid}/pokemons")
    public ResponseEntity<List<PokemonResponse>> getEntrenadorPokemons(@PathVariable("uuid") String entrenadorUuid) {
        List<PokemonResponse> response = entrenadorService.getPokemonsByUuid(entrenadorUuid).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /entrenador/{entrenadorUuid}/pokemons/{pokemonUuid}
     * Captures a pokemon for the entrenador (inserts into captura table)
     */
    @PostMapping("/entrenador/{entrenadorUuid}/pokemons/{pokemonUuid}")
    public ResponseEntity<Void> addPokemonToEntrenador(@PathVariable String entrenadorUuid,
                                                       @PathVariable String pokemonUuid) {
        boolean added = entrenadorService.capturarPokemon(entrenadorUuid, pokemonUuid);
        if (added) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ─── Helpers ──────────────────────────────────────────────

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

    // ─── Records / DTOs ───────────────────────────────────────

    public record LoginRequest(String nombre, String apellido) {}

    public record LoginResponse(String uuid) {}

    public record PokemonCreateRequest(
            String nombre,
            String descripcion,
            LocalDate fechaDescubrimiento,
            Integer generacion,
            Integer tipoPokemonId,   // ID numérico del tipo_pokemon
            String uuid) {}

    public record PokemonResponse(
            Integer id,
            String uuid,
            String nombre,
            String descripcion,
            LocalDate fechaDescubrimiento,
            Integer generacion,
            TipoPokemonResponse tipoPokemon) {}

    public record TipoPokemonResponse(Integer id, String uuid, String descripcion) {}
}
