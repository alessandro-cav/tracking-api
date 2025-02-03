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


    private String descricaoVaga;


    private BigDecimal valor;


    private Boolean refeicao;


    private String vestimenta;


    private Integer quantidade;

    private String observacao;


    private String responsabilidades;


    private String requisitos;


    private String advertencias;

    private String imagemVaga;

    private String iconeVaga;

    private String titulo;

    private String subtitulo;

    private String cargoVaga;

}
