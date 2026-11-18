package com.senac.api_epi.controllers;

import com.senac.api_epi.entities.Treinamento;
import com.senac.api_epi.entities.TreinamentoRealizado;
import com.senac.api_epi.service.TreinamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treinamentos")
public class TreinamentoController {
    private final TreinamentoService service;

    public TreinamentoController(TreinamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Treinamento> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Treinamento treinamento,
                                       @RequestParam Long adminId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.cadastrar(treinamento, adminId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<?> concluir(@PathVariable Long id,
                                     @RequestParam Long funcionarioId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.concluir(id, funcionarioId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/realizados/funcionario/{id}")
    public List<TreinamentoRealizado> listarRealizados(@PathVariable Long id) {
        return service.listarRealizados(id);
    }
}
