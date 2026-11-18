package com.senac.api_epi.controllers;

import com.senac.api_epi.entities.Funcionario;
import com.senac.api_epi.repositories.FuncionarioRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// 🚀 Correção do CORS: Permite o acesso do Flutter aceitando credenciais e cookies com segurança
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class FuncionarioController {

    private final FuncionarioRepository repository;

    public FuncionarioController(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/funcionarios")
    public List<Funcionario> listar() {
        return repository.findAll();
    }
}
