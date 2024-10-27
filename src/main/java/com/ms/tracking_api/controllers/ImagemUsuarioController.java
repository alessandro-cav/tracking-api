package com.ms.tracking_api.controllers;

import com.ms.tracking_api.services.CurriculoService;
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
@RequestMapping("usuarios/{idUsuarios}/imagens")
@RequiredArgsConstructor

@Tag(name = "Imagem do usuario Controller", description = "APIs relacionadas aos imagem do usuario controller")
@SecurityRequirement(name = "bearerAuth")
public class ImagemUsuarioController {

    private final CurriculoService service;


    @PostMapping
    @Operation(summary = "Upload de imagem", description = "Endpoint para upload de um único imagem")
    public ResponseEntity<Void> salvar(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                       @RequestParam("imagem") String imagem) {
        this.service.salvar(idUsuarios, imagem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar imagens do usuario", description = "Endpoint para listar as imagens do usuario")
    public ResponseEntity<List<String>> buscarTodos(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                    @RequestParam Integer pagina,
                                                    @RequestParam Integer quantidade,
                                                    @RequestParam String ordem,
                                                    @RequestParam String ordenarPor) {
        return ResponseEntity.ok(service.buscarTodos(idUsuarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idImagem}")
    @Operation(summary = "Buscar imagem do usuario", description = "Endpoint para buscar imagem do usuario")
    public ResponseEntity<String> buscarPeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idImagem") Long idImagem) {
        return ResponseEntity.ok(this.service.buscarPeloId(idUsuarios, idImagem));
    }

    @DeleteMapping("/{idArquivo}")
    @Operation(summary = "Deletar imagem do usuario", description = "Endpoint para  imagem do usuario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idImagem") Long idImagem) {
        this.service.deletePeloId(idUsuarios, idImagem);
        return ResponseEntity.noContent().build();
    }
}



