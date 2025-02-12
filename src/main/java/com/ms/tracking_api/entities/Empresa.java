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
@Table(name = "tb_empresa")
@SequenceGenerator(name = "SQ_EMPRESA", allocationSize = 1, sequenceName = "SQ_EMPRESA")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_EMPRESA")
    private Long idEmpresa;

    private String nome;

    private String cnpj;

    private String telefone;

    private String email;

    @Embedded
    private Endereco endereco;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imagem;

    @OneToMany(mappedBy = "empresa")
    private List<Evento> eventos;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
