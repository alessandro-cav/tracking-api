package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.StatusCandidatura;
import com.ms.tracking_api.enuns.StatusVaga;
import com.ms.tracking_api.enuns.TipoAcesso;
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
@Table(name = "tb_usuario_vaga")
@SequenceGenerator(name = "SQ_USUARIO_VAGA", allocationSize = 1, sequenceName = "SQ_USUARIO_VAGA")
public class UsuarioVaga implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USUARIO_VAGA")
    private Long idUsuarioVaga;

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
