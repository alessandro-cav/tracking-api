package com.ms.tracking_api.dtos.requests;

import com.ms.tracking_api.enuns.StatusVaga;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

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
    private BigDecimal valor;

    @NotNull(message = "O campo refeição é obrigatório.")
    private Boolean refeicao;

    @NotNull(message = "O campo vestimenta é obrigatório.")
    @NotBlank(message = "O campo vestimenta não pode ser vazio.")
    private String vestimenta;

    @NotNull(message = "O campo quantidade de vagas disponivel é obrigatório.")
    private Integer quantidade;

    private String observacao;


    private StatusVaga statusVaga;
}
