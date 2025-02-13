package com.ms.tracking_api.controllers;

import com.ms.tracking_api.services.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
@Tag(name = "Notificacoes Controller", description = "APIs relacionadas as notificacoes controller")
@SecurityRequirement(name = "bearerAuth")
public class NotificacaoController {


    private final NotificacaoService notificacaoService;

    @PostMapping("/enviarParaUsuario")
    @Operation(summary = "enviar para um usuario", description = "Endpoint para enviar notificação para um usuario")
    public ResponseEntity<Map<String, String>> sendNotificationToUser(@RequestParam Long idUsuario,
                                                                      @RequestParam String title,
                                                                      @RequestParam String body) {
            Map<String, String> response = this.notificacaoService.sendNotificationToUser(idUsuario, title, body);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/enviarNotificacaoParaTodosUsuarios")
    @Operation(summary = "Enviar notificacao para todos Usuarios", description = "Endpoint para enviar noticações para todos")
    public ResponseEntity<Map<String, String>> enviarNotificacaoParaTodos(
            @RequestParam String title,
            @RequestParam String body) {

        Map<String, String> response = notificacaoService.sendMessageToAll(title, body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    @Operation(summary = "Testar os token de enviar notificação", description = "Endpoint teste envio")
    public ResponseEntity<String> sendNotification(@RequestParam String recipientToken,
                                                   @RequestParam String title,
                                                   @RequestParam String body) {
        boolean isSent = this.notificacaoService.sendMessage(recipientToken, title, body);

        if (isSent) {
            return ResponseEntity.ok("Notification sent successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification.");
        }
    }
}

