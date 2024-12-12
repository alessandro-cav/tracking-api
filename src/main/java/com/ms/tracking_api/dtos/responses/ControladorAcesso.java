package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControladorAcesso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idFuncionario;
    private String tipoAtividade;
   private  Long idVaga;

}
