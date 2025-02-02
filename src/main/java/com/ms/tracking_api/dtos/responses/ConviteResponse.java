package com.ms.tracking_api.dtos.responses;


import com.ms.tracking_api.enuns.StatusConvite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConviteResponse implements Serializable {

    private static final long serialVersionUID = 1L;


    private String nome;

    private String email;

    private StatusConvite statusConvite;

}
