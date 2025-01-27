package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.requests.ValidarConviteRequest;
import com.ms.tracking_api.services.ConviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convites")
@RequiredArgsConstructor
@Tag(name = "Convites Controller", description = "APIs relacionadas ao convite para usuario novo controller")
@SecurityRequirement(name = "bearerAuth")
public class ConviteController {

    private final ConviteService service;

    @PostMapping("/enviarConvite")
    @Operation(summary = "Enviar convite ao Usuário", description = "Endpoint para enviar convite a um novo usuário")
    public ResponseEntity<Void> enviarConvite(@Valid  @RequestBody ConviteRequest requestDTO) {
        service.enviarConvite(requestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validarConvite")
    @Operation(summary = "Valida o  convite do Usuário", description = "Endpoint para valida o convite de um novo usuário")
    public ResponseEntity<Boolean> validarConvite(@Valid @RequestBody ValidarConviteRequest requestDTO) {
        return ResponseEntity.ok( service.validarConvite(requestDTO));
    }

    @PostMapping("/reenviarConvite")
    @Operation(summary = "Reenviar o  convite do Usuário", description = "Endpoint para reenviar o convite de um novo usuário")
    public ResponseEntity<Boolean> reenviar(@Valid @RequestBody ValidarConviteRequest requestDTO) {
        return ResponseEntity.ok( service.reenviarConvite(requestDTO));
    }
}
