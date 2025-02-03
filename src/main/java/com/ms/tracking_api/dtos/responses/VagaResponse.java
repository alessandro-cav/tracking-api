package com.ms.tracking_api.dtos.responses;


import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.RegistrarAtividade;
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
public class VagaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idVaga;

    private Integer quantidade;

    private BigDecimal valor;

    private Boolean refeicao;

    private String vestimenta;

    private String observacao;

    private StatusVaga statusVaga;

    private EventoResponse evento;

    private String descricaoVaga;

    private String responsabilidades;

    private String requisitos;

    private String advertencias;

    private String imagemVaga;

    private String iconeVaga;

    private String titulo;

    private String subtitulo;

    private String cargoVaga;
}
