package com.ms.tracking_api.dtos.requests;


import com.ms.tracking_api.entities.Vaga;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioVagaRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idFuncionarioVaga;

    private Long idVaga;

    private Long idFuncionario;

}
