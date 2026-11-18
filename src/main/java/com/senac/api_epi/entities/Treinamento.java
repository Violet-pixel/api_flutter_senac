package com.senac.api_epi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Treinamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    // Caminho do material, por exemplo /arquivos/nr06.pdf.
    @Column(length = 200)
    private String arquivoUrl;

    // Desafio extra: null significa que o treinamento não vence.
    private Integer validadeMeses;
}
