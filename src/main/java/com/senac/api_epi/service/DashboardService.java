package com.senac.api_epi.service;

import com.senac.api_epi.entities.Funcionario;
import com.senac.api_epi.repositories.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final EpiRepository epiRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TreinamentoRepository treinamentoRepository;
    private final TreinamentoRealizadoRepository realizadoRepository;

    public DashboardService(EpiRepository epiRepository, EmprestimoRepository emprestimoRepository,
                            FuncionarioRepository funcionarioRepository, TreinamentoRepository treinamentoRepository,
                            TreinamentoRealizadoRepository realizadoRepository) {
        this.epiRepository = epiRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.treinamentoRepository = treinamentoRepository;
        this.realizadoRepository = realizadoRepository;
    }

    public Map<String, Long> indicadores(Long adminId) {
        Funcionario usuario = funcionarioRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if (!"ADMIN".equals(usuario.getPerfil())) {
            throw new SecurityException("Apenas administradores podem ver o painel");
        }
        long funcionarios = funcionarioRepository.count();
        long treinamentos = treinamentoRepository.count();
        long concluidos = realizadoRepository.countParesConcluidos();
        Map<String, Long> dados = new LinkedHashMap<>();
        dados.put("episCadastrados", epiRepository.count());
        dados.put("episEmprestados", emprestimoRepository.countByStatus("EMPRESTADO"));
        dados.put("episDevolvidos", emprestimoRepository.countByStatus("DEVOLVIDO"));
        dados.put("caVencidos", epiRepository.countByValidadeCaBefore(LocalDate.now()));
        dados.put("solicitacoesPendentes", emprestimoRepository.countByStatus("SOLICITADO"));
        dados.put("treinamentosPendentes", Math.max(0, funcionarios * treinamentos - concluidos));
        dados.put("funcionariosCadastrados", funcionarios);
        dados.put("estoqueBaixo", epiRepository.countByQuantidadeDisponivelLessThanEqual(2));
        return dados;
    }

    public Map<String, Long> indicadoresFuncionario(Long funcionarioId) {
        Funcionario usuario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
        if (!"FUNCIONARIO".equals(usuario.getPerfil())) {
            throw new SecurityException("Apenas funcionários podem consultar este painel");
        }
        Map<String, Long> dados = new LinkedHashMap<>();
        dados.put("meusEpisEmUso", emprestimoRepository.countByFuncionarioIdAndStatus(funcionarioId, "EMPRESTADO"));
        dados.put("meusTreinamentosPendentes", Math.max(0,
                treinamentoRepository.count() - realizadoRepository.countTreinamentosDistintosPorFuncionario(funcionarioId)));
        return dados;
    }
}
