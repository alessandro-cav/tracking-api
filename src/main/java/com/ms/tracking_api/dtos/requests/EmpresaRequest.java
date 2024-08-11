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
public class EmpresaRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEmpresa;

    @NotNull(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo cnpj é obrigatório.")
    @NotBlank(message = "O campo cnpj não pode ser vazio.")
    private String cnpj;

    @NotNull(message = "O campo telefone é obrigatório.")
    @NotBlank(message = "O campo telefone não pode ser vazio.")
    private String telefone;

    @NotNull(message = "O campo email é obrigatório.")
    @NotBlank(message = "O campo email não pode ser vazio.")
    private String email;

}
