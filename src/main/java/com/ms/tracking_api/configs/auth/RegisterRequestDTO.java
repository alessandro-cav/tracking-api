package com.ms.tracking_api.configs.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotNull(message = "O nome é obrigatório.")
    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O email  é obrigatório.")
    @NotBlank(message = "O email  não pode ser vazio.")
    private String email;

    @NotNull(message = "A senha é obrigatória.")
    @NotBlank(message = "A senha não pode ser vazia.")
    private String senha;

}
