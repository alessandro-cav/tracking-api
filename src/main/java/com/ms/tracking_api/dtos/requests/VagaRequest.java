package com.ms.tracking_api.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VagaRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idVaga;

    private Long idEvento;

    @NotNull(message = "O campo vaga é obrigatório.")
    @NotBlank(message = "O campo vaga não pode ser vazio.")
    private String vaga;

    @NotNull(message = "O campo cachê é obrigatório.")
    @NotBlank(message = "O campo cachê não pode ser vazio.")
    private String chache;

    @NotNull(message = "O campos vestimenta é obrigatório.")
    @NotBlank(message = "O campos vestimenta não pode ser vazio.")
    private String vestimenta;

    private String observacao;
}
