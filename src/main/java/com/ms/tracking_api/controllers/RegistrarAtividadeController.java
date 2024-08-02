package com.ms.tracking_api.controllers;

import com.ms.tracking_api.configs.tokenConfig.TokenConfig;
import com.ms.tracking_api.dtos.requests.RegistrarAtividadeRequest;
import com.ms.tracking_api.dtos.requests.TokenRequest;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
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

    private final TokenConfig tokenConfig;


    @PostMapping("/gerarQrcode")
    @Operation(summary = "Registrar gerarQrcode", description = "Endpoint para gerarQrcode")
    public ResponseEntity<byte[]> gerarQrCodeEntrada(@RequestBody RegistrarAtividadeRequest request) {
        return ResponseEntity.ok(this.service.gerarQrCode(request));
    }


    @PostMapping("/registrarAtividade")
    @Operation(summary = "Registrar atividade", description = "Endpoint para registrar atividade")
    public ResponseEntity<RegistrarAtividaderResponse> registrarAtividade(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(this.service.registrarAtividade(request));
    }

    @PostMapping("/gerarComprovante")
    @Operation(summary = "Gerar comprovante", description = "Endpoint para gerar comprovante")
    public ResponseEntity<String> gerarComprovante(@RequestBody RegistrarAtividadeRequest request) {
        return ResponseEntity.ok(this.service.gerarComprovante(request));
    }
}
