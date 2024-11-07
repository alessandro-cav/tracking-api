package com.ms.tracking_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;

}
