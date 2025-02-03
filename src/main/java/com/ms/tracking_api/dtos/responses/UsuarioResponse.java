package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse implements Serializable {

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

    private LocalDate validadeASO;

    private String curriculo;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;

}
