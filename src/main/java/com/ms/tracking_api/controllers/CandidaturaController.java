package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.CandidaturaRequest;
import com.ms.tracking_api.dtos.responses.CandidaturaResponse;
import com.ms.tracking_api.services.CandidaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidaturas")
@RequiredArgsConstructor

@Tag(name = "Candidatura Controller", description = "APIs relacionadas candidatura controller")
@SecurityRequirement(name = "bearerAuth")
public class CandidaturaController {

    private final CandidaturaService service;

    @PostMapping
    @Operation(summary = "Criar a candidatura", description = "Endpoint para candidatar o usuário a vaga do evento")
    public ResponseEntity<Void> salvarUsuarioVaga(@RequestBody CandidaturaRequest request) {
        service.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Desvincular a candidatura", description = "Endpoint para descandidatar o usuário da vaga do evento")
    public ResponseEntity<Void> excluirUsuarioVaga(@RequestBody CandidaturaRequest request) {
        service.excluirPeloUsuarioVagaPeloVagaEUsuario(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/aceitar")
    @Operation(summary = "Aceitar candidatura da vaga", description = "Endpoint para aceitar candidatura do usuario a vaga do evento")
    public ResponseEntity<Void> aceitarCandidatura(@Valid  @RequestBody CandidaturaRequest request) {
        service.aceitarCandidatura(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/recusar")
    @Operation(summary = "Recusar candidatura da vaga", description = "Endpoint para recusar candidatura do usuario a vaga do evento")
    public ResponseEntity<Void> recusarCandidatura(@Valid @RequestBody CandidaturaRequest request) {
        service.recusarCandidatura(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/buscarPorTipoDeCandidatura")
    @Operation(summary = "Buscar todos os candidatos ", description = "Endpoint para buscar todos os candidatos pelo status de candidatura")
    public ResponseEntity<CandidaturaResponse> buscarPorTipoDeCandidatura(@RequestParam Integer pagina,
                                                                          @RequestParam Integer quantidade,
                                                                          @RequestParam String ordem,
                                                                          @RequestParam String ordenarPor,
                                                                          @RequestParam Long idVaga,
                                                                          @RequestParam String statusCandidatura) {
        return ResponseEntity.ok(this.service
                .buscarPorTipoDeCandidatura(idVaga, statusCandidatura, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }


    @GetMapping("/buscarUsuariosPorVaga")
    @Operation(summary = "Buscar vagas de um determinado usuario", description = "Buscar vagas de um determinado usuario")
    public ResponseEntity<CandidaturaResponse> buscarUsuariosPorVaga(
            @RequestParam Integer pagina,
            @RequestParam Integer quantidade,
            @RequestParam String ordem,
            @RequestParam String ordenarPor,
            @RequestParam Long idVaga) {
        return ResponseEntity.ok(this.service
                .buscarUsuariosPorVaga(idVaga, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }
    @GetMapping("/buscarVagasPorUsuario")
    @Operation(summary = "Buscar vagas de um determinado usuario", description = "Buscar vagas de um determinado usuario")
    public ResponseEntity<CandidaturaResponse> buscarVagasPorUsuario(
            @RequestParam Integer pagina,
            @RequestParam Integer quantidade,
            @RequestParam String ordem,
            @RequestParam String ordenarPor,
            @RequestParam Long idUsuario) {
        return ResponseEntity.ok(this.service
                .buscarVagasPorUsuario(idUsuario, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

}
