package com.ms.tracking_api.dtos.responses;

import com.ms.tracking_api.enuns.ChavePix;
import com.ms.tracking_api.enuns.Genero;
import com.ms.tracking_api.enuns.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idFuncionario;

    private String nome;

    private String cpf;

    private String email;

    private LocalDate dataNascimento;

    private Genero genero;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cep;

    private String cidade;

    private String estado;

    private String banco;

    private String agencia;

    private TipoConta tipoConta;

    private ChavePix chavePix;
}
