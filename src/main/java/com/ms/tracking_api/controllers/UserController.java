package com.ms.tracking_api.controllers;

import com.ms.tracking_api.dtos.requests.FiltroUsuarioRequest;
import com.ms.tracking_api.dtos.requests.IdRequest;
import com.ms.tracking_api.dtos.requests.UsuarioRequest;
import com.ms.tracking_api.dtos.responses.UserResponse;
import com.ms.tracking_api.dtos.responses.UsuarioResponse;
import com.ms.tracking_api.services.UserService;
import com.ms.tracking_api.services.UsuarioService;
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
@RequestMapping("/users")
@RequiredArgsConstructor

@Tag(name = "User Controller", description = "APIs relacionadas users controller web")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService service;


    @GetMapping("/web")
    @Operation(summary = "Listar usuario web", description = "Endpoint para listar usuario web")
    public ResponseEntity<List<UserResponse>> buscarTodosWeb(@RequestParam Integer pagina,
                                                             @RequestParam Integer quantidade,
                                                             @RequestParam String ordem,
                                                             @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service
                .buscarTodosWeb(PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));

    }

    @PutMapping("/inativar")
    @Operation(summary = "Inativar Usuário", description = "Endpoint para inativar um usuário pelo id")
    public ResponseEntity<Void> inativarUsuario(@RequestBody IdRequest requestDTO) {
        service.inativarUser(requestDTO.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ativar")
    @Operation(summary = "Ativar Usuário", description = "Endpoint para ativar um usuário pelo id")
    public ResponseEntity<Void> ativarUsuario(@RequestBody IdRequest requestDTO) {
        service.ativarUser(requestDTO.getId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/filtro/web")
    public ResponseEntity<List<UserResponse>> filtroUsuarioWeb(
            @RequestBody FiltroUsuarioRequest filtroUsuarioRequest, @RequestParam Integer pagina,
            @RequestParam Integer quantidade, @RequestParam String ordem, @RequestParam String ordenarPor) {
        return ResponseEntity.ok(this.service.filtroUsuarioWeb(filtroUsuarioRequest,
                PageRequest.of(pagina, quantidade, Sort.by(Sort.Direction.valueOf(ordem), ordenarPor))));
    }
}

