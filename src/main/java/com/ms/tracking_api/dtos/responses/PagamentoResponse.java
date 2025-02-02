package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idPagamento;

}
