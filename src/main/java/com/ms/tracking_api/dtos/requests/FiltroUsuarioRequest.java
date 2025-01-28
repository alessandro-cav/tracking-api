package com.ms.tracking_api.dtos.requests;


import com.ms.tracking_api.enuns.StatusUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroUsuarioRequest {

    private Long idUsuario;

    private String nome;

    private String email;

    private StatusUsuario statusUsuario;
}
