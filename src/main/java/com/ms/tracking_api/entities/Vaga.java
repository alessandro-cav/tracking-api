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
@Table(name = "tb_vaga")
@SequenceGenerator(name = "SQ_VAGA", allocationSize = 1, sequenceName = "SQ_VAGA")
public class Vaga  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_VAGA")
    private Long idVaga;

    private String vaga;

    private String chache;

    private String vestimenta;

    private String observacao;

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
