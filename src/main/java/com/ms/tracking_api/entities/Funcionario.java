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

    private String cpf;

    private String telefone;

    private String email;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @OneToMany(mappedBy = "funcionario")
    private List<RegistrarAtividade> registrarAtividades;

    @OneToMany(mappedBy = "funcionario")
    private List<Endereco> enderecos;

    @OneToMany(mappedBy = "funcionario")
    private List<Conta> contas;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
