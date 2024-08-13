package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.ChavePix;
import com.ms.tracking_api.enuns.Genero;
import com.ms.tracking_api.enuns.TipoConta;
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

    private String email;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;

    private String banco;

    private String agencia;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @Enumerated(EnumType.STRING)
    private ChavePix chavePix;

    /*    @Lob
    private List<String> documentos;*/


    @OneToMany(mappedBy = "funcionario")
    private List<RegistrarAtividade> registrarAtividades;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
