package com.ms.tracking_api.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VagaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idVaga;

    private String vaga;

    private Integer quantidade;

    private String chache;

    private String vestimenta;

    private String observacao;

    private EventoResponse evento;
}
