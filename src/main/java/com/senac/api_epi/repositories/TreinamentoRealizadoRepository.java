package com.senac.api_epi.repositories;

import com.senac.api_epi.entities.TreinamentoRealizado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TreinamentoRealizadoRepository
        extends JpaRepository<TreinamentoRealizado, Long> {
    List<TreinamentoRealizado> findByFuncionarioIdOrderByDataConclusaoDesc(
            Long funcionarioId);

    boolean existsByFuncionarioIdAndTreinamentoId(
            Long funcionarioId, Long treinamentoId);

    // Desafio extra: consulta a conclusão mais recente ao verificar a validade.
    Optional<TreinamentoRealizado> findFirstByFuncionarioIdAndTreinamentoIdOrderByDataConclusaoDesc(
            Long funcionarioId, Long treinamentoId);
    // Evita contar duas vezes um treinamento refeito pelo mesmo funcionário.
    @Query("select count(distinct r.treinamento.id) from TreinamentoRealizado r where r.funcionario.id = :funcionarioId")
    long countTreinamentosDistintosPorFuncionario(@Param("funcionarioId") Long funcionarioId);

    @Query(value = "SELECT COUNT(DISTINCT funcionario_id, treinamento_id) FROM treinamento_realizado", nativeQuery = true)
    long countParesConcluidos();
}
