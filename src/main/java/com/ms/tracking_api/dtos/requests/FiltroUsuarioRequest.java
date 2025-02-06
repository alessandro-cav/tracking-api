package com.ms.tracking_api.dtos.requests;


import com.ms.tracking_api.enuns.Status;
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

    private Status status;
}
