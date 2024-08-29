package com.ms.tracking_api.configs.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenhasRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{senha01.not.blank}")
    @NotNull(message = "{senha01.not.null}")
    private String senha01;

    @NotBlank(message = "{senha02.not.blank}")
    @NotNull(message = "{senha02.not.null}")
    private String senha02;

}
