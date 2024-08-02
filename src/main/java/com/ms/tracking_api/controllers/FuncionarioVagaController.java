package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.FuncionarioVagaRequest;
import com.ms.tracking_api.services.FuncionarioVagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionariosVagas")
@RequiredArgsConstructor

@Tag(name = "Funcionario vagas Controller", description = "APIs relacionadas funcionario vagas controller")
@SecurityRequirement(name = "bearerAuth")
public class FuncionarioVagaController {

    private final FuncionarioVagaService service;

    @PostMapping
    @Operation(summary = "Vincular funcionario da vaga", description = "Endpoint para vincular funcionario da vaga")
    public ResponseEntity<Void> salvarFuncionarioVaga(@RequestBody FuncionarioVagaRequest request) {
        service.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Desvincular funcionario da vaga", description = "Endpoint para desvincular funcionario da vaga")
    public ResponseEntity<Void> excluirFuncionarioVaga(@RequestBody FuncionarioVagaRequest request) {
        service.excluirPeloAlunoMateiraPeloAlunoEMateria(request);
        return ResponseEntity.noContent().build();
    }
}
