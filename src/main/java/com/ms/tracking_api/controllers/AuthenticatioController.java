package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.AuthenticationRequestDTO;
import com.ms.tracking_api.dtos.responses.AuthenticationResponseDTO;
import com.ms.tracking_api.services.AuthenticationService;
import com.ms.tracking_api.dtos.requests.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "APIs relacionadas à autenticação")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticatioController {

    private final AuthenticationService service;

    @PostMapping("/register")
    @Operation(summary = "Registrar Usuario da web", description = "Endpoint para registrar um novo usuário na web")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(service.register(requestDTO));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Autenticar Usuario", description = "Endpoint para autenticar um usuário")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(service.authenticate(requestDTO));
    }
}
