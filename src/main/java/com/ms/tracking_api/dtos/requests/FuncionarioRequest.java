package com.ms.tracking_api.dtos.requests;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioRequest implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long idFuncionario;

        @NotNull(message = "O campo nome é obrigatório.")
        @NotBlank(message = "O campo nome não pode ser vazio.")
        private String nome;

        @NotNull(message = "O campo rg é obrigatório.")
        @NotBlank(message = "O campo rg não pode ser vazio.")
        private String rg;

        @NotNull(message = "O campo cpf é obrigatório.")
        @NotBlank(message = "O campo cpf não pode ser vazio.")
        private String cpf;

        @NotNull(message = "O campo chave pix é obrigatório.")
        @NotBlank(message = "O campo chave pix não pode ser vazio.")
        private String chavePix;

        @NotNull(message = "O campo email é obrigatório.")
        @NotBlank(message = "O campo email não pode ser vazio.")
        private String email;

        @NotNull(message = "A lista de documentos é obrigatória.")
        @NotBlank(message = "lista não pode ser vazia.")
        @NotEmpty(message = "A lista de documentos não pode estar vazia.")
        private List<String> documentos;
}
