package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idAvalicao;

    private Integer estrela;

    private String comentario;

    private Long idUsuario;
    
}
