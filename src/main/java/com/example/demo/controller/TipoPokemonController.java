package com.example.demo.controller;

import com.example.demo.entity.TipoPokemon;
import com.example.demo.service.TipoPokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController
// @RequestMapping("/api/tipos-pokemon")
// @CrossOrigin(origins = "*")
public class TipoPokemonController {

    private final TipoPokemonService tipoPokemonService;

    @Autowired
    public TipoPokemonController(TipoPokemonService tipoPokemonService) {
        this.tipoPokemonService = tipoPokemonService;
    }

    @GetMapping
    public List<TipoPokemon> getAll() {
        return tipoPokemonService.findAll();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TipoPokemon> getByUuid(@PathVariable String uuid) {
        return tipoPokemonService.findByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TipoPokemon create(@RequestBody TipoPokemon tipoPokemon) {
        return tipoPokemonService.save(tipoPokemon);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<TipoPokemon> update(@PathVariable String uuid, @RequestBody TipoPokemon details) {
        return tipoPokemonService.findByUuid(uuid)
                .map(existing -> {
                    existing.setDescripcion(details.getDescripcion());
                    TipoPokemon updated = tipoPokemonService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable String uuid) {
        if (tipoPokemonService.deleteByUuid(uuid)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
