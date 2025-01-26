package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.StatusCandidatura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_candidatura")
public class Candidatura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCandidatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga")
    private Vaga vaga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private StatusCandidatura statusCandidatura;

    @CreationTimestamp
    private LocalDate dataCriacao;
}
