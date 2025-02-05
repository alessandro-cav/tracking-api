package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCandidatoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String evento;

    private String descricaoVaga;

    private Long idUsuario;

    private String nome;

    private String cpf;

    private String rg;

    private String telefone;

    private String email;

    private String imagem;
}
