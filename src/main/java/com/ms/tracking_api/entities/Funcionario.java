package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.Genero;
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
@Table(name = "tb_funcionario")
@SequenceGenerator(name = "SQ_FUNCIONARIO", allocationSize = 1, sequenceName = "SQ_FUNCIONARIO")
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FUNCIONARIO")
    private Long idFuncionario;

    private String nome;

    private String rg;

    private String cpf;

    private String chavePix;

    private String email;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    /*    @Lob
    private List<String> documentos;*/

    @ManyToOne
    @JoinColumn(name = "endereco")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "conta")
    private Conta conta;

    @OneToMany(mappedBy = "funcionario")
    private List<RegistrarAtividade> registrarAtividades;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
