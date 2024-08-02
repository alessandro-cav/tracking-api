package com.ms.tracking_api.controllers;

import com.ms.tracking_api.configs.email.EmailService;
import com.ms.tracking_api.configs.email.LoginRequestDTO;
import com.ms.tracking_api.configs.email.MensagemResponseDTO;
import com.ms.tracking_api.configs.email.SenhasRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recuperar")
@Tag(name = "recuperar Controller", description = "APIs relacionadas à recuperar")
public class RecuperarSenhaController {

    private final EmailService service;

    public RecuperarSenhaController(EmailService service) {
        this.service = service;
    }

    @Operation(summary = "Recuperar", description = "Endpoint para recuperar senha")
    @PostMapping("/forgot_password")
    public ResponseEntity<Void> esqueciMinhaSenha(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        this.service.esqueciMinhaSenha(loginRequestDTO);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Nova senha", description = "Endpoint para inclusão da nova senha")
    @PostMapping("/reset_password")
    public ResponseEntity<MensagemResponseDTO> resetarSenha(@RequestParam String token,
                                                            @RequestBody @Valid SenhasRequestDTO senhasRequestDTO) {
        return ResponseEntity.ok(this.service.resetarSenha(token, senhasRequestDTO));
    }
}
