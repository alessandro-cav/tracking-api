package com.ms.tracking_api.dtos.responses;

import com.ms.tracking_api.enuns.Role;
import com.ms.tracking_api.enuns.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    private Long id;

    private String nome;

    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;
}
