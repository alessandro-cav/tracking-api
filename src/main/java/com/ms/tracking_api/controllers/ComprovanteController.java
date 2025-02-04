package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.ComprovanteRequest;
import com.ms.tracking_api.dtos.responses.ComprovanteResponse;
import com.ms.tracking_api.services.ComprovanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comprovantes")
@RequiredArgsConstructor

@Tag(name = "Comrpovante Controller", description = "APIs relacionado aos comprovantes controller")
@SecurityRequirement(name = "bearerAuth")
public class ComprovanteController {

    private final ComprovanteService service;

    @PostMapping("/gerarComprovante")
    @Operation(summary = "Gerar comprovante", description = "Endpoint para gerar comprovante")
    public ResponseEntity<ComprovanteResponse> gerarComprovante(@RequestBody ComprovanteRequest request) {
        return ResponseEntity.ok(this.service.gerarComprovante(request));
    }

    @GetMapping("/comprovante-servico/{idUsuario}")
    @Operation(summary = "LIstar comprovante de serviço", description = "Endpoint para listar comprovante de serviços pelo id do usario")
    public ResponseEntity<List<ComprovanteResponse>> gerarComprovante(@PathVariable(name = "idUsuario") Long idUsuario) {
        return ResponseEntity.ok(this.service.gerarComprovanteServico(idUsuario));
    }
}
