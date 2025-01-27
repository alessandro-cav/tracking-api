package com.ms.tracking_api.dtos.responses;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private Long idEvento;

    private String nome;

    private String horaInicio;

    private String horaFim;

    private LocalDate data;

    private EmpresaResponse empresa;

    private String imagem;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;
}