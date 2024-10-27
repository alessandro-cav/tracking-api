package com.ms.tracking_api.controllers;

import com.ms.tracking_api.services.CurriculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios/{idUsuarios}/curriculos")
@RequiredArgsConstructor

@Tag(name = "curriculos do usuario Controller", description = "APIs relacionadas aos curriculos do usuario controller")
@SecurityRequirement(name = "bearerAuth")
public class CurriculoUsuarioController {

    private final CurriculoService service;

    @PostMapping
    @Operation(summary = "Upload de curriculos", description = "Endpoint para upload de um único curriculos")
    public ResponseEntity<Void> salvar(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                       @RequestParam("curriculo") String curriculo) {
        this.service.salvar(idUsuarios, curriculo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar curriculos do usuario", description = "Endpoint para listar as curriculos do usuario")
    public ResponseEntity<List<String>> buscarTodos(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                    @RequestParam Integer pagina,
                                                    @RequestParam Integer quantidade,
                                                    @RequestParam String ordem,
                                                    @RequestParam String ordenarPor) {
        return ResponseEntity.ok(service.buscarTodos(idUsuarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idCurriculo}")
    @Operation(summary = "Buscar curriculos do usuario", description = "Endpoint para buscar curriculos do usuario")
    public ResponseEntity<String> buscarPeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idCurriculo") Long idCurriculo) {
        return ResponseEntity.ok(this.service.buscarPeloId(idUsuarios, idCurriculo));
    }

    @DeleteMapping("/{idCurriculo}")
    @Operation(summary = "Deletar curriculos do usuario", description = "Endpoint para curriculos conta do usuario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idCurriculo") Long idCurriculo) {
        this.service.deletePeloId(idUsuarios, idCurriculo);
        return ResponseEntity.noContent().build();
    }
}



