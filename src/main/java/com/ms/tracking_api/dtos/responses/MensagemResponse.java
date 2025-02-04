package com.ms.tracking_api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MensagemResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
}
