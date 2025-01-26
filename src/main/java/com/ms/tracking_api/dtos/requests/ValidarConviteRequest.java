package com.ms.tracking_api.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidarConviteRequest implements Serializable {

    private static final long serialVersionUID = 1L;


    @NotNull(message = "O campo código é obrigatório.")
    @NotBlank(message = "O campo código não pode ser vazio.")
    private String codigo;

    @NotNull(message = "O campo email  é obrigatório.")
    @NotBlank(message = "O campo email  não pode ser vazio.")
    private String email;
}
