package com.ms.tracking_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_arquivo")
@SequenceGenerator(name = "SQ_ARQUIVO", allocationSize = 1, sequenceName = "SQ_ARQUIVO")
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ARQUIVO")
    private Long idArquivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario")
    private Funcionario funcionario;

    private String nome;

    private String tipo;

    private Long tamanho;

    @Lob
    @Column(columnDefinition = "TEXT") // ou BLOB dependendo do tipo de dado
    private String arquivo;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
