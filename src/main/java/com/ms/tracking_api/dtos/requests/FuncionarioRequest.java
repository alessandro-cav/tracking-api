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
public class FuncionarioRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idFuncionario;

    @NotNull(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo cpf é obrigatório.")
    @NotBlank(message = "O campo cpf não pode ser vazio.")
    private String cpf;

    @NotNull(message = "O campo telefone é obrigatório.")
    @NotBlank(message = "O campo telefone não pode ser vazio.")
    private String telefone;

    @NotNull(message = "O campo logradouro é obrigatório.")
    @NotBlank(message = "O campo logradouro não pode ser vazio.")
    private String email;

    @NotNull(message = "O campo data de nascimento é obrigatório.")
    private LocalDate dataNascimento;

    @NotNull(message = "O campo logradouro é obrigatório.")
    @NotBlank(message = "O campo logradouro não pode ser vazio.")
    private String genero;

}
