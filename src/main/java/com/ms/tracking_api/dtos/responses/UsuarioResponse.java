package com.ms.tracking_api.dtos.responses;

import com.ms.tracking_api.enuns.Genero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuario;

    private String nome;

    private String cpf;

    private String telefone;

    private String email;

    private LocalDate dataNascimento;

    private Genero genero;

}
