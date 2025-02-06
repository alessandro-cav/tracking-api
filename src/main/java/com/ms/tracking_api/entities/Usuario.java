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
@Table(name = "tb_usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long idUsuario;

    private String nome;

    private String cpf;

    private String cnpjBanco;

    private String rg;

    private String telefone;

    private String email;

    private LocalDate dataNascimento;

    private String genero;

    private LocalDate validadeASO;

    @OneToMany(mappedBy = "usuario")
    private List<RegistrarAtividade> registrarAtividades;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "usuario")
    private List<Conta> contas;

    @OneToMany(mappedBy = "usuario")
    private List<Avaliacao> avaliacao;

    @OneToMany(mappedBy = "usuario")
    private List<Comprovante> recibos;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String curriculo;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String aso;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imagem;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
