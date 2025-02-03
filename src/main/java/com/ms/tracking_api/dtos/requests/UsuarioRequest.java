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

    private String nome;


    private String cpf;


    private String rg;


    private String telefone;


    private String email;


    private LocalDate dataNascimento;

    private String genero;

    private String imagem;

    private String aso;

    private String curriculo;

    private LocalDate validadeASO;


    private String logradouro;


    private String numero;


    private String bairro;


    private String cep;


    private String cidade;


    private String estado;
}
