package com.ms.tracking_api.dtos.responses;

import com.ms.tracking_api.enuns.StatusCandidatura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidaturaUsuarioResponse implements Serializable {

    private String nomeEvento;

    private String horaInicio;

    private String horaFim;

    private LocalDate data;

    private String descricaoVaga;

    private StatusCandidatura statusCandidatura;
}
