package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidaturaResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<UsuarioCandidatoResponse> usuarios;

    private Integer quantidade;

    private List<VagaCandidatoResponse> vagas;
}
