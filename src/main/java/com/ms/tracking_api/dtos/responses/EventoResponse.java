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

    private String horario;

    private LocalDate data;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;

    private EmpresaResponse empresa;
}