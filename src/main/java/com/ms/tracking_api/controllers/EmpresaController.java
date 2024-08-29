package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.EmpresaRequest;
import com.ms.tracking_api.dtos.responses.EmpresaResponse;
import com.ms.tracking_api.services.EmpresaService;
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
@RequestMapping("/empresas")
@RequiredArgsConstructor

@Tag(name = "Empresa Controller", description = "APIs relacionadas a empresa controller")
@SecurityRequirement(name = "bearerAuth")
public class EmpresaController {

    private final EmpresaService service;

    @PostMapping
    @Operation(summary = "Salvar a empresa", description = "Endpoint para salvar empresa")
    public ResponseEntity<EmpresaResponse> salvar(@Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.ok(this.service.salvar(request));
    }


    @GetMapping
    @Operation(summary = "Listar a empresa", description = "Endpoint para listar empresa")
    public ResponseEntity<List<EmpresaResponse>> buscarTodos(@RequestParam Integer pagina,
                                                             @RequestParam Integer quantidade,
                                                             @RequestParam String ordem,
                                                             @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar a empresa pelo id", description = "Endpoint para buscar empresa")
    public ResponseEntity<EmpresaResponse> buscarPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.buscarPeloId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar a empresa", description = "Endpoint para deletar empresa")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "id") Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar a empresa", description = "Endpoint para =atualizar empresa")
    public ResponseEntity<EmpresaResponse> atualizar(@PathVariable(name = "id") Long id,
                                                     @Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.ok(this.service.atualizar(id, request));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar a empresa por nome", description = "Endpoint para buscar empresa")
    public ResponseEntity<List<EmpresaResponse>> buscarPorNome(@RequestParam Integer pagina,
                                                               @RequestParam Integer quantidade,
                                                               @RequestParam String ordem,
                                                               @RequestParam String ordenarPor,
                                                               @RequestParam String nome) {
        return ResponseEntity.ok(this.service
                .buscarPorNome(nome, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }
}
