package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.ContaRequest;
import com.ms.tracking_api.dtos.responses.ContaResponse;
import com.ms.tracking_api.services.ContaService;
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
@RequestMapping("/{idFuncionarios}/contas")
@RequiredArgsConstructor

@Tag(name = "Conta do funcionario Controller", description = "APIs relacionadas  conta do funcionario controller")
@SecurityRequirement(name = "bearerAuth")
public class ContaFuncionarioController {

    private final ContaService service;

    @PostMapping
    @Operation(summary = "Salvar o conta do funcionario", description = "Endpoint para salvar  conta do funcionario")
    public ResponseEntity<ContaResponse> salvar(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @Valid @RequestBody ContaRequest request) {
        return ResponseEntity.ok(this.service.salvarContaDoFuncionario(idFuncionarios, request));
    }

    @GetMapping
    @Operation(summary = "Listar conta do funcionario", description = "Endpoint para listar conta do funcionario")
    public ResponseEntity<List<ContaResponse>> buscarTodos(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                              @RequestParam Integer pagina,
                                                              @RequestParam Integer quantidade,
                                                              @RequestParam String ordem,
                                                              @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodasContaDoFuncionario(idFuncionarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idConta}")
    @Operation(summary = "Buscar conta do funcionario pelo id", description = "Endpoint para buscar conta do funcionario")
    public ResponseEntity<ContaResponse> buscarPeloId(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @PathVariable(name = "idConta") Long idConta) {
        return ResponseEntity.ok(this.service.buscarContaDoFuncionarioPeloId(idFuncionarios, idConta));
    }

    @DeleteMapping("/{idConta}")
    @Operation(summary = "Deletar conta do funcionario", description = "Endpoint para deletar conta do funcionario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @PathVariable(name = "idConta") Long idConta) {
        this.service.deleteContaDoFuncionario(idFuncionarios,idConta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idConta}")
    @Operation(summary = "Atualizar  conta do funcionario", description = "Endpoint para atualizar conta do funcionario")
    public ResponseEntity<ContaResponse> atualizar(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                   @PathVariable(name = "idConta") Long idConta,
                                                   @Valid @RequestBody ContaRequest request) {
        return ResponseEntity.ok(this.service.atualizarContaDoFuncionario(idFuncionarios,idConta, request));
    }

    @GetMapping("/buscarPorTipoConta")
    @Operation(summary = "Buscar o conta do funcionario por tipo conta", description = "Endpoint para buscar conta do funcionario")
    public ResponseEntity<List<ContaResponse>> buscarPorNome(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                             @RequestParam Integer pagina,
                                                             @RequestParam Integer quantidade,
                                                             @RequestParam String ordem,
                                                             @RequestParam String ordenarPor,
                                                             @RequestParam String tipoPix) {
        return ResponseEntity.ok(this.service
                .buscarContaDoFuncionarioPorTipoConta(idFuncionarios,tipoPix, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }
}
