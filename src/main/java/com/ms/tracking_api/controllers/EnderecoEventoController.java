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
@RequestMapping("/{idEventos}/enderecos")
@RequiredArgsConstructor

@Tag(name = "Endereco do evento Controller", description = "APIs relacionadas o endereco do evento controller")
@SecurityRequirement(name = "bearerAuth")
public class EnderecoEventoController {

    private final EnderecoService service;

    @PostMapping
    @Operation(summary = "Salvar o endereço do evento", description = "Endpoint para salvar o endereço do evento")
    public ResponseEntity<EnderecoResponse> salvar(@PathVariable(name = "idEventos") Long idEventos, @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(this.service.salvarEnderecoDoEvento(idEventos, request));
    }

    @GetMapping
    @Operation(summary = "Listar o endereço do evento", description = "Endpoint para listar o endereço do evento")
    public ResponseEntity<List<EnderecoResponse>> buscarTodos(@PathVariable(name = "idEventos") Long idEventos,
                                                              @RequestParam Integer pagina,
                                                              @RequestParam Integer quantidade,
                                                              @RequestParam String ordem,
                                                              @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodosEnderecoDoEvento(idEventos, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idEndereco}")
    @Operation(summary = "Buscar o endereço do evento pelo id", description = "Endpoint para buscar o endereço do evento")
    public ResponseEntity<EnderecoResponse> buscarPeloId(@PathVariable(name = "idEventos") Long idEventos, @PathVariable(name = "idEndereco") Long idEndereco) {
        return ResponseEntity.ok(this.service.buscarEnderecoDoEventoPeloId(idEventos, idEndereco));
    }

    @DeleteMapping("/{idEndereco}")
    @Operation(summary = "Deletar o endereço do evento", description = "Endpoint para deletar o endereço do evento")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idEventos") Long idEventos, @PathVariable(name = "idEndereco") Long idEndereco) {
        this.service.deleteEnderecoDoEvento(idEventos,idEndereco);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idEndereco}")
    @Operation(summary = "Atualizar o endereço do evento", description = "Endpoint para atualizar o endereço do evento")
    public ResponseEntity<EnderecoResponse> atualizar(@PathVariable(name = "idEventos") Long idEventos,
                                                      @PathVariable(name = "idEndereco") Long idEndereco,
                                                      @Valid @RequestBody EnderecoRequest request) {
        return ResponseEntity.ok(this.service.atualizarEnderecoDoEvento(idEventos,idEndereco, request));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar o endereço do evento por nome", description = "Endpoint para buscar o endereço do evento")
    public ResponseEntity<List<EnderecoResponse>> buscarPorNome(@PathVariable(name = "idEventos") Long idEventos,
                                                                @RequestParam Integer pagina,
                                                                @RequestParam Integer quantidade,
                                                                @RequestParam String ordem,
                                                                @RequestParam String ordenarPor,
                                                                @RequestParam String logradouro) {
        return ResponseEntity.ok(this.service
                .buscarEnderecoDoEventoPorNome(idEventos,logradouro, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

}
