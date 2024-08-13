package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.EventoRequest;
import com.ms.tracking_api.dtos.requests.VagaRequest;
import com.ms.tracking_api.dtos.responses.EventoResponse;
import com.ms.tracking_api.dtos.responses.VagaResponse;
import com.ms.tracking_api.services.EventoService;
import com.ms.tracking_api.services.VagaService;
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
@RequestMapping("/vagas")
@RequiredArgsConstructor

@Tag(name = "Vagas Controller", description = "APIs relacionadas as vagas controller")
@SecurityRequirement(name = "bearerAuth")
public class VagaController {

    private final VagaService service;

    @PostMapping
    @Operation(summary = "Criar a vaga", description = "Endpoint para criar vaga")
    public ResponseEntity<VagaResponse> salvar(@Valid @RequestBody VagaRequest request) {
        return ResponseEntity.ok(this.service.salvar(request));
    }


  @GetMapping
  @Operation(summary = "Listar vaga", description = "Endpoint para listar vaga")
    public ResponseEntity<List<VagaResponse>> buscarTodos(@RequestParam Integer pagina,
                                                                  @RequestParam Integer quantidade,
                                                                  @RequestParam String ordem,
                                                                  @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar vaga pelo id", description = "Endpoint para buscar vaga")
    public ResponseEntity<VagaResponse> buscarPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.buscarPeloId(id));
    }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletar vaga", description = "Endpoint para deletar vaga")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "id") Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vaga", description = "Endpoint para atualizar vaga")
    public ResponseEntity<VagaResponse> atualizar(@PathVariable(name = "id") Long id,
                                                          @Valid @RequestBody VagaRequest request) {
        return ResponseEntity.ok(this.service.atualizar(id, request));
    }

    @GetMapping("/buscarPorVaga")
    @Operation(summary = "Buscar vaga por vaga", description = "Endpoint para buscar vaga")
    public ResponseEntity<List<VagaResponse>> buscarPorNome (@RequestParam Integer pagina,
                                                           @RequestParam Integer quantidade,
                                                           @RequestParam String ordem,
                                                           @RequestParam String ordenarPor,
                                                              @RequestParam String vaga) {
        return ResponseEntity.ok(this.service
                .buscarPorNome(vaga, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }
}
