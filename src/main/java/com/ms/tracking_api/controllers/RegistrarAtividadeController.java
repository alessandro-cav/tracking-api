package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.QRCodeRequest;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
import com.ms.tracking_api.services.RegistrarAtividadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/registroAtividades/{idUsuario}")
    @Operation(summary = "Lista de atividades", description = "Endpoint para listar as atividades pelo id do usuario")
    public ResponseEntity<List<RegistrarAtividaderResponse>> listarRegistroAtividades(@PathVariable(name = "idUsuario") Long idUsuario) {
        return ResponseEntity.ok(this.service.listarRegistroAtividades(idUsuario));
    }
}
