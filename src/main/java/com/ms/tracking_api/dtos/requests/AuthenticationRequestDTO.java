package com.ms.tracking_api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

    private String senha;

    private String FcmToken;
}
