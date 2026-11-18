package com.senac.api_epi.controllers;

import com.senac.api_epi.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> indicadores(@RequestParam Long adminId) {
        try {
            return ResponseEntity.ok(service.indicadores(adminId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/funcionario/{id}")
    public ResponseEntity<?> indicadoresFuncionario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.indicadoresFuncionario(id));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
