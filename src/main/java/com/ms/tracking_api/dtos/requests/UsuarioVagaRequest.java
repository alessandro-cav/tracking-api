package com.ms.tracking_api.dtos.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioVagaRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuarioVaga;

    private Long idVaga;

    private Long idUsuario;

}
