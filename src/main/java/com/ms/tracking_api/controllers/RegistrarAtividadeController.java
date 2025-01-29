package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.ReciboRequest;
import com.ms.tracking_api.dtos.requests.QRCodeRequest;
import com.ms.tracking_api.dtos.responses.ReciboResponse;
import com.ms.tracking_api.services.RegistrarAtividadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registrarAtividades")
@RequiredArgsConstructor

@Tag(name = "Registrar atividade Controller", description = "APIs relacionado a registrar atividade controller")
@SecurityRequirement(name = "bearerAuth")
public class RegistrarAtividadeController {

    private final RegistrarAtividadeService service;


    @PostMapping("/registrarAtividade")
    @Operation(summary = "Registrar atividade", description = "Endpoint para registrar atividade")
    public ResponseEntity<String> registrarAtividade(@RequestBody QRCodeRequest request) {
        return ResponseEntity.ok(this.service.registrarAtividade(request));
    }

    @PostMapping("/gerarComprovante")
    @Operation(summary = "Gerar comprovante", description = "Endpoint para gerar comprovante")
    public ResponseEntity<ReciboResponse> gerarComprovante(@RequestBody ReciboRequest request) {
        return ResponseEntity.ok(this.service.gerarComprovante(request));
    }
}
