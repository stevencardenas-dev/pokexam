package com.example.demo.controller;

import com.example.demo.entity.Entrenador;
import com.example.demo.service.EntrenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController
// @RequestMapping("/api/entrenadores")
// @CrossOrigin(origins = "*")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    @Autowired
    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @GetMapping
    public List<Entrenador> getAll() {
        return entrenadorService.findAll();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Entrenador> getByUuid(@PathVariable String uuid) {
        return entrenadorService.findByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Entrenador create(@RequestBody Entrenador entrenador) {
        return entrenadorService.save(entrenador);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Entrenador> update(@PathVariable String uuid, @RequestBody Entrenador details) {
        return entrenadorService.findByUuid(uuid)
                .map(existing -> {
                    existing.setNombre(details.getNombre());
                    existing.setApellido(details.getApellido());
                    existing.setFechaNacimiento(details.getFechaNacimiento());
                    existing.setFechaVinculacion(details.getFechaVinculacion());
                    existing.setPueblo(details.getPueblo());
                    if (details.getPokemons() != null) {
                        existing.setPokemons(details.getPokemons());
                    }
                    Entrenador updated = entrenadorService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable String uuid) {
        if (entrenadorService.deleteByUuid(uuid)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{entrenadorUuid}/capturar/{pokemonUuid}")
    public ResponseEntity<Void> capturar(@PathVariable String entrenadorUuid, @PathVariable String pokemonUuid) {
        if (entrenadorService.capturarPokemon(entrenadorUuid, pokemonUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{entrenadorUuid}/liberar/{pokemonUuid}")
    public ResponseEntity<Void> liberar(@PathVariable String entrenadorUuid, @PathVariable String pokemonUuid) {
        if (entrenadorService.liberarPokemon(entrenadorUuid, pokemonUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
