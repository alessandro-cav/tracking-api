package com.ms.tracking_api.dtos.responses;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEmpresa;

    private String nome;

    private String cnpj;

    private String telefone;

    private String email;

    private String imagem;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;
}
