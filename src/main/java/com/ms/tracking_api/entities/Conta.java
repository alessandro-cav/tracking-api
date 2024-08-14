package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.Pix;
import com.ms.tracking_api.enuns.TipoConta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_conta")
@SequenceGenerator(name = "SQ_CONTA", allocationSize = 1, sequenceName = "SQ_CONTA")
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CONTA")
    private Long idConta;

    private String banco;

    private String agencia;

    private String  numero;

    @Enumerated(EnumType.STRING)
    private Pix pix;

    private String chavePix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario")
    private Funcionario funcionario;
}
