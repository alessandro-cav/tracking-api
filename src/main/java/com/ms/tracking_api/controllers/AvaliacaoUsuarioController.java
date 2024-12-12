package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.AvaliacaoRequest;
import com.ms.tracking_api.dtos.requests.ContaRequest;
import com.ms.tracking_api.dtos.responses.AvaliacaoResponse;
import com.ms.tracking_api.services.AvaliacaoService;
import com.ms.tracking_api.services.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{idUsuarios}/avaliacoes")
@RequiredArgsConstructor

@Tag(name = "Avaliação do usuario Controller", description = "APIs relacionadas  avaliação do usuario controller")
@SecurityRequirement(name = "bearerAuth")
public class AvaliacaoUsuarioController {

    private final AvaliacaoService service;

    @PostMapping
    @Operation(summary = "Salvar a avaliação do usuario", description = "Endpoint para salvar  avaliação do usuario")
    public ResponseEntity<AvaliacaoResponse> salvar(@PathVariable(name = "idUsuarios") Long idUsuarios, @Valid @RequestBody AvaliacaoRequest request) {
        return ResponseEntity.ok(this.service.salvarAvaliacaoDoUsuario(idUsuarios, request));
    }

}
