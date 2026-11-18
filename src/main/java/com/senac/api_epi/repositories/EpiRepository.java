package com.senac.api_epi.repositories;
import com.senac.api_epi.entities.Epi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
public interface EpiRepository extends JpaRepository<Epi, Long> {

    long countByValidadeCaBefore(LocalDate data);
    long countByQuantidadeDisponivelLessThanEqual(int limite);
}