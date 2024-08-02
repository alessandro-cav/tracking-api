package com.ms.tracking_api.dtos.requests;

import com.ms.tracking_api.entities.Empresa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long IdEvento;

    @NotNull(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo local é obrigatório.")
    @NotBlank(message = "O campo local não pode ser vazio.")
    private String local;

    @NotNull(message = "O campo horario é obrigatório.")
    @NotBlank(message = "O campo horario não pode ser vazio.")
    private String horario;

    @NotNull(message = "O campo data é obrigatório.")
    @NotBlank(message = "O campo data não pode ser vazio.")
    private String data;


    private Long idEmpresa;
}