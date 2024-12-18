package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.responses.ConviteResponse;
import com.ms.tracking_api.services.ConviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/convites")
@RequiredArgsConstructor
@Tag(name = "Convites Controller", description = "APIs relacionadas ao convite para usuario novo controller")
@SecurityRequirement(name = "bearerAuth")
public class ConviteController {

    private final ConviteService service;

    @PostMapping
    @Operation(summary = "Convite Usuario", description = "Endpoint para convidar um novo usuário")
    public ResponseEntity<ConviteResponse> convite(
            @RequestBody ConviteRequest requestDTO) {
        return ResponseEntity.ok(service.criarUsuarioConvidado(requestDTO));
    }

}
