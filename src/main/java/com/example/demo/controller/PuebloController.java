package com.example.demo.controller;

import com.example.demo.entity.Pueblo;
import com.example.demo.service.PuebloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController
// @RequestMapping("/api/pueblos")
// @CrossOrigin(origins = "*")
public class PuebloController {

    private final PuebloService puebloService;

    @Autowired
    public PuebloController(PuebloService puebloService) {
        this.puebloService = puebloService;
    }

    @GetMapping
    public List<Pueblo> getAll() {
        return puebloService.findAll();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Pueblo> getByUuid(@PathVariable String uuid) {
        return puebloService.findByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pueblo create(@RequestBody Pueblo pueblo) {
        return puebloService.save(pueblo);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Pueblo> update(@PathVariable String uuid, @RequestBody Pueblo puebloDetails) {
        return puebloService.findByUuid(uuid)
                .map(existingPueblo -> {
                    existingPueblo.setNombre(puebloDetails.getNombre());
                    Pueblo updated = puebloService.save(existingPueblo);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable String uuid) {
        if (puebloService.deleteByUuid(uuid)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
