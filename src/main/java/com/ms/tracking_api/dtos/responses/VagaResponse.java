package com.ms.tracking_api.dtos.responses;


import com.ms.tracking_api.enuns.StatusVaga;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

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

}
