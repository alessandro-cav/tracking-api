package com.ms.tracking_api.configs.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponseDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String messagem;

    public static MensagemResponseDTO getMenssagem(String mensagem) {
        return new MensagemResponseDTO(mensagem);
    }
}
