package com.ms.tracking_api.dtos.requests;

import com.ms.tracking_api.enuns.StatusConvite;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroConviteRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idConvite;

    private String codigo;

    private String email;

    private String nome;

    private StatusConvite statusConvite;
}
