package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.EventoRequest;
import com.ms.tracking_api.dtos.requests.IdRequest;
import com.ms.tracking_api.dtos.responses.EventoResponse;
import com.ms.tracking_api.dtos.responses.EventoVagaResponse;
import com.ms.tracking_api.services.EventoService;
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
@RequestMapping("/eventos")
@RequiredArgsConstructor

@Tag(name = "Evento Controller", description = "APIs relacionadas a evento controller")
@SecurityRequirement(name = "bearerAuth")
public class EventoController {

    private final EventoService service;

    @PostMapping
    @Operation(summary = "Criar a evento", description = "Endpoint para criar evento")
    public ResponseEntity<EventoResponse> salvar(@Valid @RequestBody EventoRequest request) {
        return ResponseEntity.ok(this.service.salvar(request));
    }


    @GetMapping("/ListarTodos")
    @Operation(summary = "Listar eventos antigos e novos", description = "Endpoint para listar eventos antigos e novos")
    public ResponseEntity<List<EventoResponse>> buscarTodos(@RequestParam Integer pagina,
                                                            @RequestParam Integer quantidade,
                                                            @RequestParam String ordem,
                                                            @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

    @GetMapping("/ListarNovos")
    @Operation(summary = "Listar eventos novos", description = "Endpoint para listar eventos  novos")
    public ResponseEntity<List<EventoResponse>> buscarNovoEventos(@RequestParam Integer pagina,
                                                                  @RequestParam Integer quantidade,
                                                                  @RequestParam String ordem,
                                                                  @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarNovoEventos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento pelo id", description = "Endpoint para buscar evento")
    public ResponseEntity<EventoResponse> buscarPeloId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.buscarPeloId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento", description = "Endpoint para deletar evento")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "id") Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Endpoint para atualizar evento")
    public ResponseEntity<EventoResponse> atualizar(@PathVariable(name = "id") Long id,
                                                    @Valid @RequestBody EventoRequest request) {
        return ResponseEntity.ok(this.service.atualizar(id, request));
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar evento por nome", description = "Endpoint para buscar evento")
    public ResponseEntity<List<EventoResponse>> buscarPorNome(@RequestParam Integer pagina,
                                                              @RequestParam Integer quantidade,
                                                              @RequestParam String ordem,
                                                              @RequestParam String ordenarPor,
                                                              @RequestParam String nome) {
        return ResponseEntity.ok(this.service
                .buscarPorNome(nome, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

    @GetMapping("/{id}/vagas")
    @Operation(summary = "Buscar vagas pelo id do evento", description = "Endpoint para buscar vagas pelo id do evento")
    public ResponseEntity<List<EventoVagaResponse>> buscarVagasPeloIdEvento(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok((this.service.buscarVagasPeloIdEvento(id)));
    }

    @PutMapping("/fechar")
    @Operation(summary = "Fechar evento", description = "Endpoint para fechar um evento pelo id")
    public ResponseEntity<Void> fecharEvento(@RequestBody IdRequest requestDTO) {
        service.fecharEvento(requestDTO.getId());
        return ResponseEntity.ok().build();
    }

}
