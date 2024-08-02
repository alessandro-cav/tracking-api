package com.ms.tracking_api.dtos.responses;

import com.ms.tracking_api.enuns.TipoAtividade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarAtividaderResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRegistrarAtividade;

    private TipoAtividade tipoAtividade;

    private String nomeFuncinario;

    private String vaga;

    private String evento;

    private Long idFuncionario;

    private Long idVaga;

}
