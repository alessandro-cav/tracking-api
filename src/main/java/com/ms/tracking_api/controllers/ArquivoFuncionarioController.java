package com.ms.tracking_api.controllers;

import com.ms.tracking_api.services.ArquivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/{idFuncionarios}/arquivos")
@RequiredArgsConstructor

@Tag(name = "Arquivo do funcionario Controller", description = "APIs relacionadas aos arquivos do funcionario controller")
@SecurityRequirement(name = "bearerAuth")
public class ArquivoFuncionarioController {

    private final ArquivoService service;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload de arquivo", description = "Endpoint para upload de um único arquivo")
    public ResponseEntity<Void> salvar(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                       @RequestParam("arquivo") MultipartFile arquivo) {
        this.service.salvar(idFuncionarios, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar arquivos do funcionário", description = "Endpoint para listar as arquivos do funcionário")
    public ResponseEntity<List<String>> buscarTodos(@PathVariable(name = "idFuncionarios") Long idFuncionarios,
                                                    @RequestParam Integer pagina,
                                                    @RequestParam Integer quantidade,
                                                    @RequestParam String ordem,
                                                    @RequestParam String ordenarPor) {
        return ResponseEntity.ok(service.buscarTodos(idFuncionarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idArquivo}")
    @Operation(summary = "Buscar arquivo do funcionario", description = "Endpoint para buscar arquivo do funcionario")
    public ResponseEntity<String> buscarPeloId(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @PathVariable(name = "idArquivo") Long idArquivo) {
        return ResponseEntity.ok(this.service.buscarPeloId(idFuncionarios, idArquivo));
    }

    @DeleteMapping("/{idArquivo}")
    @Operation(summary = "Deletar arquivo do funcionario", description = "Endpoint para arquivo conta do funcionario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idFuncionarios") Long idFuncionarios, @PathVariable(name = "idArquivo") Long idArquivo) {
        this.service.deletePeloId(idFuncionarios, idArquivo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{arquivo}")
    @Operation(summary = "Baixar arquivo", description = "Endpoint para baixar um arquivo")
    public ResponseEntity<org.springframework.core.io.Resource> downloadArquivo(
            @PathVariable(name = "idFuncionarios") Long idFuncionarios,
            @PathVariable Long arquivo) {
        Resource resource = this.service.downloadArquivo(idFuncionarios, arquivo);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}



