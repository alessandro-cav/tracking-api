package com.ms.tracking_api.controllers;

import com.ms.tracking_api.services.AsoService;
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
@RequestMapping("usuarios/{idUsuarios}/asos")
@RequiredArgsConstructor

@Tag(name = "asos do usuario Controller", description = "APIs relacionadas aos asos do usuario controller")
@SecurityRequirement(name = "bearerAuth")
public class ASOUsuarioController {

    private final AsoService service;


    @PostMapping
    @Operation(summary = "Upload de asos", description = "Endpoint para upload de um único asos")
    public ResponseEntity<Void> salvar(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                       @RequestParam("aso") String aso) {
        this.service.salvar(idUsuarios, aso);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar curriculos do asos", description = "Endpoint para listar as asos do usuario")
    public ResponseEntity<List<String>> buscarTodos(@PathVariable(name = "idUsuarios") Long idUsuarios,
                                                    @RequestParam Integer pagina,
                                                    @RequestParam Integer quantidade,
                                                    @RequestParam String ordem,
                                                    @RequestParam String ordenarPor) {
        return ResponseEntity.ok(service.buscarTodos(idUsuarios, PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }

    @GetMapping("/{idAso}")
    @Operation(summary = "Buscar asos do usuario", description = "Endpoint para buscar asos do usuario")
    public ResponseEntity<String> buscarPeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idAso") Long idAso) {
        return ResponseEntity.ok(this.service.buscarPeloId(idUsuarios, idAso));
    }

    @DeleteMapping("/{idAso}")
    @Operation(summary = "Deletar asos do usuario", description = "Endpoint para asos conta do usuario")
    public ResponseEntity<Void> deletePeloId(@PathVariable(name = "idUsuarios") Long idUsuarios, @PathVariable(name = "idAso") Long idAso) {
        this.service.deletePeloId(idUsuarios, idAso);
        return ResponseEntity.noContent().build();
    }
}



