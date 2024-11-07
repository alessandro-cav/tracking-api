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
@Table(name = "tb_usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long idUsuario;

    private String nome;

    private String cpf;

    private String telefone;

    private String email;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @OneToMany(mappedBy = "usuario")
    private List<RegistrarAtividade> registrarAtividades;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "usuario")
    private List<Conta> contas;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String curriculo;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aso;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imagem;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
