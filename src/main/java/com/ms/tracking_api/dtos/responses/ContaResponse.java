package com.ms.tracking_api.dtos.responses;


import com.ms.tracking_api.enuns.TipoChave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idConta;

    private String titularConta;

    private String banco;

    private String agencia;

    private String numeroConta;

    private String chavePix;

    private String tipoChave;

    private String tipoConta;
}
