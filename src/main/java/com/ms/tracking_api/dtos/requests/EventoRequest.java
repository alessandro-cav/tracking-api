package com.ms.tracking_api.dtos.requests;

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
public class EventoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long IdEvento;

    private Long idEmpresa;

    @NotNull(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo horario é obrigatório.")
    @NotBlank(message = "O campo horario não pode ser vazio.")
    private String horario;

    @NotNull(message = "O campo data é obrigatório.")
    private LocalDate data;

}