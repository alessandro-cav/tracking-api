package com.ms.tracking_api.configs.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class SenhasRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{senha01.not.blank}")
    @NotNull(message = "{senha01.not.null}")
    private String senha01;

    @NotBlank(message = "{senha02.not.blank}")
    @NotNull(message = "{senha02.not.null}")
    private String senha02;

    public String getSenha01() {
        return senha01;
    }

    public void setSenha01(String senha01) {
        this.senha01 = senha01;
    }

    public String getSenha02() {
        return senha02;
    }

    public void setSenha02(String senha02) {
        this.senha02 = senha02;
    }
}
