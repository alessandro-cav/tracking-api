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
@RequestMapping("/usuarios/{idUsuarios}/contas")
@RequiredArgsConstructor

@Tag(name = "Conta do usuario Controller", description = "APIs relacionadas  conta do usuario controller")
@SecurityRequirement(name = "bearerAuth")
public class ContaUsuarioController {

    private final ContaService service;

    @PostMapping
    @Operation(summary = "Salvar o conta do usuario", description = "Endpoint para salvar  conta do usuario")
    public ResponseEntity<ContaResponse> salvar(@PathVariable(name = "idUsuarios") Long idUsuarios, @Valid @RequestBody ContaRequest request) {
        return ResponseEntity.ok(this.service.salvarContaDoUsuario(idUsuarios, request));
    }

    @GetMapping
    @Operation(summary = "Listar conta do usuario", description = "Endpoint para listar conta do usuario")
    public ResponseEntity<List<ContaResponse>> buscarTodos(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                           @RequestParam Integer pagina,
                                                           @RequestParam Integer quantidade,
                                                           @RequestParam String ordem,
                                                           @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodasContaDoUsuario(idUsuarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idConta}")
    @Operation(summary = "Buscar conta do usuario pelo id", description = "Endpoint para buscar conta do usuario")
    public ResponseEntity<ContaResponse> buscarPeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idConta") Long idConta) {
        return ResponseEntity.ok(this.service.buscarContaDoUsuarioPeloId(idUsuarios, idConta));
    }

    @DeleteMapping("/{idConta}")
    @Operation(summary = "Deletar conta do usuario", description = "Endpoint para deletar conta do usuario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idConta") Long idConta) {
        this.service.deleteContaDoUsuario(idUsuarios, idConta);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idConta}")
    @Operation(summary = "Atualizar  conta do usuario", description = "Endpoint para atualizar conta do usuario")
    public ResponseEntity<ContaResponse> atualizar(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                   @PathVariable(name = "idConta") Long idConta,
                                                   @Valid @RequestBody ContaRequest request) {
        return ResponseEntity.ok(this.service.atualizarContaDoUsuario(idUsuarios, idConta, request));
    }

    @GetMapping("/buscarPorTipoConta")
    @Operation(summary = "Buscar o conta do usuario por tipo conta", description = "Endpoint para buscar conta do usuario")
    public ResponseEntity<List<ContaResponse>> buscarPorNome(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                             @RequestParam Integer pagina,
                                                             @RequestParam Integer quantidade,
                                                             @RequestParam String ordem,
                                                             @RequestParam String ordenarPor,
                                                             @RequestParam String tipoPix) {
        return ResponseEntity.ok(this.service
                .buscarContaDoUsuarioPorTipoChave(idUsuarios, tipoPix, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }
}
