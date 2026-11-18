package com.senac.api_epi.service;

import com.senac.api_epi.entities.Funcionario;
import com.senac.api_epi.entities.Treinamento;
import com.senac.api_epi.entities.TreinamentoRealizado;
import com.senac.api_epi.repositories.FuncionarioRepository;
import com.senac.api_epi.repositories.TreinamentoRealizadoRepository;
import com.senac.api_epi.repositories.TreinamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TreinamentoService {
    private final TreinamentoRepository treinamentoRepository;
    private final TreinamentoRealizadoRepository realizadoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public TreinamentoService(TreinamentoRepository treinamentoRepository,
                             TreinamentoRealizadoRepository realizadoRepository,
                             FuncionarioRepository funcionarioRepository) {
        this.treinamentoRepository = treinamentoRepository;
        this.realizadoRepository = realizadoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Treinamento> listar() {
        return treinamentoRepository.findAllByOrderByTituloAsc();
    }

    // Apenas ADMIN pode cadastrar.
    public Treinamento cadastrar(Treinamento treinamento, Long adminId) {
        verificarAdmin(adminId);
        if (treinamento.getTitulo() == null || treinamento.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Informe o título do treinamento");
        }
        if (treinamento.getValidadeMeses() != null && treinamento.getValidadeMeses() <= 0) {
            throw new IllegalArgumentException("A validade deve ser maior que zero");
        }
        treinamento.setId(null);
        return treinamentoRepository.save(treinamento);
    }

    public TreinamentoRealizado concluir(Long treinamentoId, Long funcionarioId) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
        Treinamento treinamento = treinamentoRepository.findById(treinamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Treinamento não encontrado"));

        Optional<TreinamentoRealizado> ultimo = realizadoRepository
                .findFirstByFuncionarioIdAndTreinamentoIdOrderByDataConclusaoDesc(
                        funcionario.getId(), treinamento.getId());

        if (ultimo.isPresent()) {
            Integer validadeMeses = treinamento.getValidadeMeses();
            boolean vencido = validadeMeses != null
                    && ultimo.get().getDataConclusao().plusMonths(validadeMeses).isBefore(LocalDate.now());
            if (!vencido) {
                throw new IllegalArgumentException("Este treinamento já foi concluído");
            }
        }

        TreinamentoRealizado registro = new TreinamentoRealizado();
        registro.setFuncionario(funcionario);
        registro.setTreinamento(treinamento);
        registro.setDataConclusao(LocalDate.now());
        return realizadoRepository.save(registro);
    }

    public List<TreinamentoRealizado> listarRealizados(Long funcionarioId) {
        return realizadoRepository.findByFuncionarioIdOrderByDataConclusaoDesc(funcionarioId);
    }

    // Mesmo controle de acesso da Aula 5.
    private void verificarAdmin(Long adminId) {
        Funcionario usuario = funcionarioRepository.findById(adminId)
                .orElseThrow(() -> new SecurityException("Usuário não encontrado"));
        if (!"ADMIN".equals(usuario.getPerfil())) {
            throw new SecurityException("Apenas administradores podem fazer esta operação");
        }
    }
}
