package com.ms.tracking_api.dtos.responses;

import com.ms.tracking_api.enuns.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarAtividaderResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRegistrarAtividade;

    private TipoAcesso tipoAcesso;

    private String nome;

    private String descricaoVaga;

    private String evento;

}
