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

    private String imagem;

    @NotNull(message = "O campo logradouro é obrigatório.")
    @NotBlank(message = "O campo logradouro não pode ser vazio.")
    private String logradouro;

    @NotNull(message = "O campo numero é obrigatório.")
    @NotBlank(message = "O campo numero não pode ser vazio.")
    private String numero;

    @NotNull(message = "O campo bairro é obrigatório.")
    @NotBlank(message = "O campo bairro não pode ser vazio.")
    private String bairro;

    @NotNull(message = "O campo cep é obrigatório.")
    @NotBlank(message = "O campo cep não pode ser vazio.")
    private String cep;

    @NotNull(message = "O campo cidade é obrigatório.")
    @NotBlank(message = "O campo cidade não pode ser vazio.")
    private String cidade;

    @NotNull(message = "O campo estado é obrigatório.")
    @NotBlank(message = "O campo estado não pode ser vazio.")
    private String estado;

}
