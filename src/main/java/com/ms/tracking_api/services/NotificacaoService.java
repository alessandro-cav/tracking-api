package com.ms.tracking_api.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.handlers.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final UserService userService;

    public Map<String, String> sendMessage(String recipientToken, String title, String body) {
        return this.enviarNotificacao(recipientToken, title, body);
    }

    public Map<String, String> sendNotificationToUser(Long idUsuario, String title, String body) {
        User user = this.userService.findById(idUsuario);

        if (user.getFcmToken() == null) {
            throw new BadRequestException("Usuário não possui um token de notificação registrado.");
        }
        return this.enviarNotificacao(user.getFcmToken(), title, body);
    }

    private static Map<String, String> enviarNotificacao(String recipientToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(recipientToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);

            return Map.of("message", "Notificação enviada com sucesso.");
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Falha ao enviar notificação para o usuário.");
        }
    }
}




