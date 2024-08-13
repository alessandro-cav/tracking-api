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
@RequestMapping("/{idFuncionarios}/enderecos")
@RequiredArgsConstructor

@Tag(name = "Endereco do funcionario Controller", description = "APIs relacionadas o endereco do funcionario controller")
@SecurityRequirement(name = "bearerAuth")
public class EnderecoFuncionarioController {

    private final EnderecoService service;

    @PostMapping
    @Operation(summary = "Salvar o endereço do funcionario", description = "Endpoint para salvar o endereço do funcionario")
    public ResponseEntity<EnderecoResponse> salvar(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(this.service.salvarEnderecoDoFuncionario(idFuncionarios, request));
    }

    @GetMapping
    @Operation(summary = "Listar o endereço do funcionario", description = "Endpoint para listar o endereço do funcionario")
    public ResponseEntity<List<EnderecoResponse>> buscarTodos(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                              @RequestParam Integer pagina,
                                                              @RequestParam Integer quantidade,
                                                              @RequestParam String ordem,
                                                              @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodosEnderecoDoFuncionario(idFuncionarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idEndereco}")
    @Operation(summary = "Buscar o endereço do funcionario pelo id", description = "Endpoint para buscar o endereço do funcionario")
    public ResponseEntity<EnderecoResponse> buscarPeloId(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @PathVariable(name = "idEndereco") Long idEndereco) {
        return ResponseEntity.ok(this.service.buscarEnderecoDoFuncionarioPeloId(idFuncionarios, idEndereco));
    }

    @DeleteMapping("/{idEndereco}")
    @Operation(summary = "Deletar o endereço do funcionario", description = "Endpoint para deletar o endereço do funcionario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @PathVariable(name = "idEndereco") Long idEndereco) {
        this.service.deleteEnderecoDoFuncionario(idFuncionarios,idEndereco);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idEndereco}")
    @Operation(summary = "Atualizar o endereço do funcionario", description = "Endpoint para atualizar o endereço do funcionario")
    public ResponseEntity<EnderecoResponse> atualizar(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                      @PathVariable(name = "idEndereco") Long idEndereco,
                                                      @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(this.service.atualizarEnderecoDoFuncionario(idFuncionarios,idEndereco, request));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar o endereço do funcionario por nome", description = "Endpoint para buscar o endereço do funcionario")
    public ResponseEntity<List<EnderecoResponse>> buscarPorNome(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                                @RequestParam Integer pagina,
                                                                @RequestParam Integer quantidade,
                                                                @RequestParam String ordem,
                                                                @RequestParam String ordenarPor,
                                                                @RequestParam String logradouro) {
        return ResponseEntity.ok(this.service
                .buscarEnderecoDoFuncionarioPorNome(idFuncionarios,logradouro, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

}
