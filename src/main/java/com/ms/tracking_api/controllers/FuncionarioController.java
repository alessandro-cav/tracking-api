package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.FuncionarioRequest;
import com.ms.tracking_api.dtos.responses.FuncionarioResponse;
import com.ms.tracking_api.services.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor

@Tag(name = "Funcionario Controller", description = "APIs relacionadas funcionario controller")
@SecurityRequirement(name = "bearerAuth")
public class FuncionarioController {

    private final FuncionarioService service;

    @PostMapping
    @Operation(summary = "Criar funcionario", description = "Endpoint para criar funcionario")
    public ResponseEntity<FuncionarioResponse> salvar(@Valid @RequestBody FuncionarioRequest request, List<MultipartFile> documentos) {
        return ResponseEntity.ok(this.service.salvar(request));
    }


  @GetMapping
  @Operation(summary = "Listar funcionario", description = "Endpoint para listar funcionario")
    public ResponseEntity<List<FuncionarioResponse>> buscarTodos(@RequestParam Integer pagina,
                                                                  @RequestParam Integer quantidade,
                                                                  @RequestParam String ordem,
                                                                  @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionario pelo id", description = "Endpoint para buscar funcionario")
    public ResponseEntity<FuncionarioResponse> buscarPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.buscarPeloId(id));
    }

   @DeleteMapping("/{id}")
   @Operation(summary = "Deletar funcionario", description = "Endpoint para deletar funcionario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "id") Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionario", description = "Endpoint para atualizar funcionario")
    public ResponseEntity<FuncionarioResponse> atualizar(@PathVariable(name = "id") Long id,
                                                          @Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.ok(this.service.atualizar(id, request));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar funcionario por nome", description = "Endpoint para buscar funcionario")
    public ResponseEntity<List<FuncionarioResponse>> buscarPorNome (@RequestParam Integer pagina,
                                                           @RequestParam Integer quantidade,
                                                           @RequestParam String ordem,
                                                           @RequestParam String ordenarPor,
                                                              @RequestParam String nome) {
        return ResponseEntity.ok(this.service
                .buscarPorNome(nome, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }
}

