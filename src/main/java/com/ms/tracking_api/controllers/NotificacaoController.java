package com.ms.tracking_api.controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.UserRepository;
import com.ms.tracking_api.services.NotificacaoService;
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

    private final UserRepository userRepository;

    @PostMapping("/enviar")
    public Map<String, String> sendNotification(@RequestParam String recipientToken,
                                                   @RequestParam String title,
                                                   @RequestParam String body) {
         return this.notificacaoService.sendMessage(recipientToken, title, body);
    }

    @PostMapping("/enviarParaUsuario")
    public ResponseEntity<Map<String, String>> sendNotificationToUser(@RequestParam Long idUsuario,
                                                                      @RequestParam String title,
                                                                      @RequestParam String body) {
            Map<String, String> response = this.notificacaoService.sendNotificationToUser(idUsuario, title, body);
            return ResponseEntity.ok(response);
    }
}

