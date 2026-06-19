package com.example.demo.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db-status")
    public Map<String, Object> dbStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            String database = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
            String user = jdbcTemplate.queryForObject("SELECT current_user", String.class);
            Integer ok = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("status", "UP");
            response.put("database", database);
            response.put("user", user);
            response.put("queryResult", ok);
        } catch (Exception ex) {
            response.put("status", "DOWN");
            response.put("error", ex.getMessage());
        }
        return response;
    }
}
