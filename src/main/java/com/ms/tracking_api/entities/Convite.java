package com.ms.tracking_api.entities;

import com.ms.tracking_api.enuns.StatusCandidatura;
import com.ms.tracking_api.enuns.StatusConvite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_convite")
public class Convite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConvite;

    private String codigo;

    private String email;

    private String nome;

    @Enumerated(EnumType.STRING)
    private StatusConvite statusConvite;
}
