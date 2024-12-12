package com.ms.tracking_api.dtos.responses;


import com.ms.tracking_api.enuns.StatusVaga;
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

    private String vaga;

    private Integer quantidade;

    private BigDecimal valor;

    private Boolean refeicao;

    private String vestimenta;

    private String observacao;

    private StatusVaga statusVaga;

    private EventoResponse evento;
}
