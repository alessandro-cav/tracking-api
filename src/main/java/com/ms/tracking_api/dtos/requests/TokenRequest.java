package com.ms.tracking_api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
}
