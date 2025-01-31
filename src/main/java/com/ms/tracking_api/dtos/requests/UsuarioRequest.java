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
public class UsuarioRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuario;

    @NotNull(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo cpf é obrigatório.")
    @NotBlank(message = "O campo cpf não pode ser vazio.")
    private String cpf;

    @NotNull(message = "O campo rg é obrigatório.")
    @NotBlank(message = "O campo rg não pode ser vazio.")
    private String rg;

    @NotNull(message = "O campo telefone é obrigatório.")
    @NotBlank(message = "O campo telefone não pode ser vazio.")
    private String telefone;

    @NotNull(message = "O campo email é obrigatório.")
    @NotBlank(message = "O campo email não pode ser vazio.")
    private String email;

    @NotNull(message = "O campo data de nascimento é obrigatório.")
    private LocalDate dataNascimento;

    @NotNull(message = "O campo genero é obrigatório.")
    @NotBlank(message = "O campo genero não pode ser vazio.")
    private String genero;

    private String imagem;

    private String aso;

    private String curriculo;

    private LocalDate validadeASO;

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
