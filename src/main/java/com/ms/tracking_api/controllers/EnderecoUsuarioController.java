package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.EnderecoRequest;
import com.ms.tracking_api.dtos.responses.EnderecoResponse;
import com.ms.tracking_api.services.EnderecoService;
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
@RequestMapping("/usuarios/{idUsuarios}/enderecos")
@RequiredArgsConstructor

@Tag(name = "Endereco do usuario Controller", description = "APIs relacionadas o endereco do usuario controller")
@SecurityRequirement(name = "bearerAuth")
public class EnderecoUsuarioController {

    private final EnderecoService service;

    @PostMapping
    @Operation(summary = "Salvar o endereço do usuario", description = "Endpoint para salvar o endereço do usuario")
    public ResponseEntity<EnderecoResponse> salvar(@PathVariable(name = "idUsuarios") Long idUsuarios, @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(this.service.salvarEnderecoDoUsuario(idUsuarios, request));
    }

    @GetMapping
    @Operation(summary = "Listar o endereço do usuario", description = "Endpoint para listar o endereço do usuario")
    public ResponseEntity<List<EnderecoResponse>> buscarTodos(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                              @RequestParam Integer pagina,
                                                              @RequestParam Integer quantidade,
                                                              @RequestParam String ordem,
                                                              @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodosEnderecoDoUsuario(idUsuarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idEndereco}")
    @Operation(summary = "Buscar o endereço do usuario pelo id", description = "Endpoint para buscar o endereço do usuario")
    public ResponseEntity<EnderecoResponse> buscarPeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idEndereco") Long idEndereco) {
        return ResponseEntity.ok(this.service.buscarEnderecoDoUsuarioPeloId(idUsuarios, idEndereco));
    }

    @DeleteMapping("/{idEndereco}")
    @Operation(summary = "Deletar o endereço do usuario", description = "Endpoint para deletar o endereço do usuario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idEndereco") Long idEndereco) {
        this.service.deleteEnderecoDoUsuario(idUsuarios,idEndereco);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idEndereco}")
    @Operation(summary = "Atualizar o endereço do usuario", description = "Endpoint para atualizar o endereço do usuario")
    public ResponseEntity<EnderecoResponse> atualizar(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                      @PathVariable(name = "idEndereco") Long idEndereco,
                                                      @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(this.service.atualizarEnderecoDoUsuario(idUsuarios,idEndereco, request));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar o endereço do usuario por nome", description = "Endpoint para buscar o endereço do usuario")
    public ResponseEntity<List<EnderecoResponse>> buscarPorNome(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                                @RequestParam Integer pagina,
                                                                @RequestParam Integer quantidade,
                                                                @RequestParam String ordem,
                                                                @RequestParam String ordenarPor,
                                                                @RequestParam String logradouro) {
        return ResponseEntity.ok(this.service
                .buscarEnderecoDoUsuarioPorNome(idUsuarios,logradouro, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

}
