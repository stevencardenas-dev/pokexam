package com.example.demo.controller;

import com.example.demo.entity.Pokemon;
import com.example.demo.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController
// @RequestMapping("/api/pokemons")
// @CrossOrigin(origins = "*")
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public List<Pokemon> getAll() {
        return pokemonService.findAll();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Pokemon> getByUuid(@PathVariable String uuid) {
        return pokemonService.findByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pokemon create(@RequestBody Pokemon pokemon) {
        return pokemonService.save(pokemon);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Pokemon> update(@PathVariable String uuid, @RequestBody Pokemon details) {
        return pokemonService.findByUuid(uuid)
                .map(existing -> {
                    existing.setNombre(details.getNombre());
                    existing.setDescripcion(details.getDescripcion());
                    existing.setTipoPokemon(details.getTipoPokemon());
                    existing.setFechaDescubrimiento(details.getFechaDescubrimiento());
                    existing.setGeneracion(details.getGeneracion());
                    Pokemon updated = pokemonService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable String uuid) {
        if (pokemonService.deleteByUuid(uuid)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
