package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.TipoAcesso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_registrar_atividade")
public class RegistrarAtividade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegistrarAtividade;

    @Enumerated(EnumType.STRING)
    private TipoAcesso tipoAcesso;

    @CreationTimestamp
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga")
    private Vaga vaga;

    private LocalDateTime horaEntrada;

    private LocalDateTime horaSaida;


}
