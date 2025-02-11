package com.ms.tracking_api.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Role;
import com.ms.tracking_api.handlers.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final UserService userService;

    public Map<String, String> sendMessageToAll(String title, String body) {
        List<User> users = userService.findAll()
                .stream()
                .filter(this::isUsuarioValido)
                .collect(Collectors.toList());

        Map<String, String> resultados = new HashMap<>();

        // Se não houver usuários válidos, retorna uma mensagem e não tenta enviar notificações
        if (users.isEmpty()) {
            resultados.put("Mensagem", "Nenhum usuário válido encontrado para envio de notificações.");
            return resultados;
        }

        List<String> falhas = new ArrayList<>();

        users.forEach(user -> {
            try {
                enviarNotificacao(user, title, body);
            } catch (FirebaseMessagingException e) {
                falhas.add(formatarMensagemErro(user, e.getMessage()));
            }
        });

        if (falhas.isEmpty()) {
            resultados.put("Mensagem", "Todas as notificações foram enviadas com sucesso.");
        } else {
            resultados.put("Mensagem", "Algumas notificações falharam.");
            resultados.put("Erros", String.join("; ", falhas));
        }

        return resultados;
    }


    public Map<String, String> sendNotificationToUser(Long idUsuario, String title, String body) {
        User user = userService.findById(idUsuario);

        if (!isUsuarioValido(user)) {
            throw new BadRequestException("Falha ao enviar notificação para " + user.getNome() +
                    " (Email: " + user.getEmail() + "): usuário inválido ou sem token de notificação.");
        }

        Map<String, String> resultado = new HashMap<>();
        try {
            enviarNotificacao(user, title, body);
            resultado.put("Mensagem", "Notificação enviada com sucesso para " + user.getNome());
        } catch (FirebaseMessagingException e) {
            resultado.put("Erro", formatarMensagemErro(user, e.getMessage()));
        }

        return resultado;
    }

    private boolean isUsuarioValido(User user) {
        return user != null && user.getFcmToken() != null && !user.getFcmToken().isEmpty()
                && user.getRole() == Role.USUARIO;
    }

    private void enviarNotificacao(User user, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        FirebaseMessaging.getInstance().send(message);
    }

    private String formatarMensagemErro(User user, String erro) {
        return "Falha ao enviar notificação para " + user.getNome() +
                " (Email: " + user.getEmail() + "). Erro: " + erro;
    }
}




