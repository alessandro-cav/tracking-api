package com.ms.tracking_api.dtos.requests;

import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.RegistrarAtividade;
import com.ms.tracking_api.enuns.StatusVaga;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VagaRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idVaga;

    private Long idEvento;

    @NotNull(message = "O campo vaga é obrigatório.")
    @NotBlank(message = "O campo vaga não pode ser vazio.")
    private String descricaoVaga;

    @NotNull(message = "O campo cachê é obrigatório.")
    private BigDecimal valor;

    @NotNull(message = "O campo refeição é obrigatório.")
    private Boolean refeicao;

    @NotNull(message = "O campo vestimenta é obrigatório.")
    @NotBlank(message = "O campo vestimenta não pode ser vazio.")
    private String vestimenta;

    @NotNull(message = "O campo quantidade de vagas disponivel é obrigatório.")
    private Integer quantidade;

    private String observacao;

    @NotNull(message = "O campo responsabilidades é obrigatório.")
    @NotBlank(message = "O campo responsabilidades não pode ser vazio.")
    private String responsabilidades;

    @NotNull(message = "O campo requisitos é obrigatório.")
    @NotBlank(message = "O campo requisitos não pode ser vazio.")
    private String requisitos;

    @NotNull(message = "O campo advertencias é obrigatório.")
    @NotBlank(message = "O campo advertencias não pode ser vazio.")
    private String advertencias;

    private String imagemVaga;

    private String iconeVaga;

    @NotNull(message = "O campo titulo é obrigatório.")
    @NotBlank(message = "O campo titulo não pode ser vazio.")
    private String titulo;

    private String subtitulo;

    @NotNull(message = "O campo cargo é obrigatório.")
    @NotBlank(message = "O campo cargo não pode ser vazio.")
    private String cargoVaga;

}
