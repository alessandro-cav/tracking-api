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

    @NotNull(message = "O campo nome é obrigatório.")
    @NotBlank(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo email  é obrigatório.")
    @NotBlank(message = "O campo email  não pode ser vazio.")
    private String email;

    @NotNull(message = "O campo senha é obrigatória.")
    @NotBlank(message = "O campo senha não pode ser vazia.")
    private String senha;

    private String role;
}
