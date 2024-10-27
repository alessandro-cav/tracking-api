package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.UsuarioVagaRequest;
import com.ms.tracking_api.services.UsuarioVagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/UsuariosVagas")
@RequiredArgsConstructor

@Tag(name = "Usuario vagas Controller", description = "APIs relacionadas Usuario vagas controller")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioVagaController {

    private final UsuarioVagaService service;

    @PostMapping
    @Operation(summary = "Vincular Usuario da vaga", description = "Endpoint para vincular Usuario da vaga")
    public ResponseEntity<Void> salvarUsuarioVaga(@RequestBody UsuarioVagaRequest request) {
        service.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Desvincular usuario da vaga", description = "Endpoint para desvincular usuario da vaga")
    public ResponseEntity<Void> excluirUsuarioVaga(@RequestBody UsuarioVagaRequest request) {
        service.excluirPeloUsuarioVagaPeloVagaEUsuario(request);
        return ResponseEntity.noContent().build();
    }
}
