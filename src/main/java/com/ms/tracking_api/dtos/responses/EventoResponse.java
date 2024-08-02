package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long IdEvento;

    private String nome;

    private String local;

    private String horario;

    private LocalDate data;

    private EmpresaResponse empresa;
}