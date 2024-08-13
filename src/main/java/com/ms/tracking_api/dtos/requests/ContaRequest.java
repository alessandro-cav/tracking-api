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

    @NotNull(message = "O campo banco é obrigatório.")
    @NotBlank(message = "O campo banco não pode ser vazio.")
    private String banco;

    @NotNull(message = "O campo agencia é obrigatório.")
    @NotBlank(message = "O campo agencia não pode ser vazio.")
    private String agencia;

    @NotNull(message = "O campo tipoConta é obrigatório.")
    @NotBlank(message = "O campo tipoConta não pode ser vazio.")
    private String tipoConta;

    @NotNull(message = "O campo chave pix é obrigatório.")
    @NotBlank(message = "O campo chave pix não pode ser vazio.")
    private String chavePix;
}
