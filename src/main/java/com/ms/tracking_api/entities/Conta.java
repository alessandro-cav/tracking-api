package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.TipoChave;
import com.ms.tracking_api.enuns.TipoConta;
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
@Table(name = "tb_conta")
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConta;

    private String titularConta;

    private String banco;

    private String agencia;

    private String  numero;

    @Enumerated(EnumType.STRING)
    private TipoChave tipoChave;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    private String chavePix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
