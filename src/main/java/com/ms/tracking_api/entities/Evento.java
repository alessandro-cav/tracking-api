package com.ms.tracking_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_evento")
@SequenceGenerator(name = "SQ_EVENTO", allocationSize = 1, sequenceName = "SQ_EVENTO")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_EVENTO")
    private Long idEvento;

    private String nome;

    private String horario;

    private LocalDate data;

    @Embedded
    private Endereco endereco;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa")
    private Empresa empresa;

    @OneToMany(mappedBy = "evento")
    private List<Vaga> vagas;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
