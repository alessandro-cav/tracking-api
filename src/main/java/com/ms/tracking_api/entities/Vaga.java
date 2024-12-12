package com.ms.tracking_api.entities;


import com.ms.tracking_api.enuns.Genero;
import com.ms.tracking_api.enuns.StatusVaga;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_vaga")
public class Vaga  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVaga;

    private String vaga;

    private BigDecimal valor;

    private String vestimenta;

    private Boolean refeicao;

    private Integer quantidade;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusVaga statusVaga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento")
    private Evento evento;

    @OneToMany(mappedBy = "vaga")
    private List<RegistrarAtividade> registrarAtividades;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
