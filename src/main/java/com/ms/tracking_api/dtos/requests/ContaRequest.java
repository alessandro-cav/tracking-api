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
public class ContaRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idConta;

    @NotNull(message = "O campo titular da conta é obrigatório.")
    @NotBlank(message = "O campo titular da conta não pode ser vazio.")
    private String titularConta;

    @NotNull(message = "O campo banco é obrigatório.")
    @NotBlank(message = "O campo banco não pode ser vazio.")
    private String banco;

    @NotNull(message = "O campo agencia é obrigatório.")
    @NotBlank(message = "O campo agencia não pode ser vazio.")
    private String agencia;

    @NotNull(message = "O campo numero da conta é obrigatório.")
    @NotBlank(message = "O campo numero da conta não pode ser vazio.")
    private String numeroConta;

    @NotNull(message = "O campo chave pix é obrigatório.")
    @NotBlank(message = "O campo chave pix não pode ser vazio.")
    private String chavePix;

    @NotNull(message = "O campo tipo chave é obrigatório.")
    @NotBlank(message = "O campo tipo chave não pode ser vazio.")
    private String tipoChave;

    @NotNull(message = "O campo tipo conta é obrigatório.")
    @NotBlank(message = "O campo tipo conta não pode ser vazio.")
    private String tipoConta;
}
