package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprovanteResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String rg;

    private String cpf;

    private BigDecimal valor;

    private LocalDate dataRecibo;
}
