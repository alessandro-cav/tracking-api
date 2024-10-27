package com.ms.tracking_api.controllers;

import com.ms.tracking_api.services.CurriculoService;
import com.ms.tracking_api.services.ImagemService;
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
@RequestMapping("empresas/{idEmpresas}/imagens")
@RequiredArgsConstructor

@Tag(name = "Imagem do empresa Controller", description = "APIs relacionadas aos imagem do empresa controller")
@SecurityRequirement(name = "bearerAuth")
public class ImagemEmpresaController {

    private final ImagemService service;


    @PostMapping
    @Operation(summary = "Upload de imagem", description = "Endpoint para upload de um único imagem")
    public ResponseEntity<Void> salvar(@PathVariable(name = "idEmpresas") Long idEmpresas,
                                       @RequestParam("imagem") String imagem) {
        this.service.salvarEmpresa(idEmpresas, imagem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar imagens do empresa", description = "Endpoint para listar as imagens do empresa")
    public ResponseEntity<List<String>> buscarTodos(@PathVariable(name = "idEmpresas") Long idEmpresas,
                                                    @RequestParam Integer pagina,
                                                    @RequestParam Integer quantidade,
                                                    @RequestParam String ordem,
                                                    @RequestParam String ordenarPor) {
        return ResponseEntity.ok(service.buscarTodosEmpresa(idEmpresas, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idImagem}")
    @Operation(summary = "Buscar imagem do empresa", description = "Endpoint para buscar imagem do empresa")
    public ResponseEntity<String> buscarPeloId(@PathVariable(name = "idEmpresas") Long idEmpresas, @PathVariable(name = "idImagem") Long idImagem) {
        return ResponseEntity.ok(this.service.buscarPeloIdEmpresa(idEmpresas, idImagem));
    }

    @DeleteMapping("/{idImagem}")
    @Operation(summary = "Deletar imagem do empresa", description = "Endpoint para  imagem do empresa")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idEmpresas") Long idEmpresas, @PathVariable(name = "idImagem") Long idImagem) {
        this.service.deletePeloIdEmpresa(idEmpresas, idImagem);
        return ResponseEntity.noContent().build();
    }
}



