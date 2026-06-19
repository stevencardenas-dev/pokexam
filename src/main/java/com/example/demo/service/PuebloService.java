package com.example.demo.service;

import com.example.demo.entity.Pueblo;
import com.example.demo.repository.PuebloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PuebloService {

    private final PuebloRepository puebloRepository;

    @Autowired
    public PuebloService(PuebloRepository puebloRepository) {
        this.puebloRepository = puebloRepository;
    }

    public List<Pueblo> findAll() {
        return puebloRepository.findAll();
    }

    public Optional<Pueblo> findById(Integer id) {
        return puebloRepository.findById(id);
    }

    public Optional<Pueblo> findByUuid(String uuid) {
        return puebloRepository.findByUuid(uuid);
    }

    public Pueblo save(Pueblo pueblo) {
        if (pueblo.getUuid() == null || pueblo.getUuid().isBlank()) {
            pueblo.setUuid(UUID.randomUUID().toString());
        }
        return puebloRepository.save(pueblo);
    }

    public void deleteById(Integer id) {
        puebloRepository.deleteById(id);
    }

    public boolean deleteByUuid(String uuid) {
        return puebloRepository.findByUuid(uuid)
            .map(pueblo -> {
                puebloRepository.delete(pueblo);
                return true;
            }).orElse(false);
    }
}
