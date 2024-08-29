package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ArquivoResponse  implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idArquivo;

    private String nome;

    private String tipo;

    private long tamanho;
}
