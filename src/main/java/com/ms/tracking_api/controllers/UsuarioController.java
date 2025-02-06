package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.UsuarioRequest;
import com.ms.tracking_api.dtos.responses.UsuarioResponse;
import com.ms.tracking_api.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

@Tag(name = "Usuarios Controller", description = "APIs relacionadas usuarios controller")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping("/mobile")
    @Operation(summary = "Criar usuario para mobile", description = "Endpoint para criar usuario para mobile")
    public ResponseEntity<UsuarioResponse> salvarMobile(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(this.service.salvarMobile(request));
    }

    @GetMapping
    @Operation(summary = "Listar usuario", description = "Endpoint para listar usuario")
    public ResponseEntity<List<UsuarioResponse>> buscarTodos(@RequestParam Integer pagina,
                                                             @RequestParam Integer quantidade,
                                                             @RequestParam String ordem,
                                                             @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario pelo id", description = "Endpoint para buscar usuario")
    public ResponseEntity<UsuarioResponse> buscarPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.buscarPeloId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuario", description = "Endpoint para deletar usuario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "id") Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Endpoint para atualizar usuario")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable(name = "id") Long id,
                                                     @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(this.service.atualizar(id, request));
    }
}

