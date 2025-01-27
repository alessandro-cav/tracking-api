package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.requests.FiltroConviteRequest;
import com.ms.tracking_api.dtos.requests.ValidarConviteRequest;
import com.ms.tracking_api.dtos.responses.ConviteResponseDTO;
import com.ms.tracking_api.services.ConviteService;
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
@RequestMapping("/convites")
@RequiredArgsConstructor
@Tag(name = "Convites Controller", description = "APIs relacionadas ao convite para usuario novo controller")
@SecurityRequirement(name = "bearerAuth")
public class ConviteController {

    private final ConviteService service;

    @PostMapping("/enviarConvite")
    @Operation(summary = "Enviar convite ao Usuário", description = "Endpoint para enviar convite a um novo usuário")
    public ResponseEntity<Void> enviarConvite(@Valid  @RequestBody ConviteRequest requestDTO) {
        service.enviarConvite(requestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validarConvite")
    @Operation(summary = "Valida o  convite do Usuário", description = "Endpoint para valida o convite de um novo usuário")
    public ResponseEntity<Boolean> validarConvite(@Valid @RequestBody ValidarConviteRequest requestDTO) {
        return ResponseEntity.ok( service.validarConvite(requestDTO));
    }

    @PostMapping("/reenviarConvite")
    @Operation(summary = "Reenviar o  convite do Usuário", description = "Endpoint para reenviar o convite de um novo usuário")
    public ResponseEntity<Boolean> reenviar(@Valid @RequestBody ConviteRequest requestDTO) {
        return ResponseEntity.ok( service.reenviarConvite(requestDTO));
    }


    @PostMapping("/filtro")
    public ResponseEntity<List<ConviteResponseDTO>> filtroUsuario(
            @RequestBody FiltroConviteRequest filtroConviteRequestDTO, @RequestParam Integer pagina,
            @RequestParam Integer quantidade, @RequestParam String ordem, @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service.filtroConvite(filtroConviteRequestDTO,
                PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }
    @GetMapping
    @Operation(summary = "Listar  convite", description = "Endpoint para listar convite")
    public ResponseEntity<List<ConviteResponseDTO>> buscarTodos(@RequestParam Integer pagina,
                                                                @RequestParam Integer quantidade,
                                                                @RequestParam String ordem,
                                                                @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodos(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

}
