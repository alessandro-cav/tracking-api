package com.ms.tracking_api.dtos.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idFuncionario;

    private String nome;

    private String rg;

    private String cpf;

    private String chavePix;

 /*   private String documento;*/

}
