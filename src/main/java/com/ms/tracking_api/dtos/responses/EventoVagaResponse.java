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
public class EventoVagaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idVaga;

    private String descricaoVaga;

    private String responsabilidades;

    private String requisitos;

    private String advertencias;

    private String imagemVaga;

    private String iconeVaga;

    private BigDecimal valor;

    private String vestimenta;

    private Boolean refeicao;

    private Integer quantidade;

    private String observacao;

    private StatusVaga statusVaga;

}
