package com.ms.tracking_api.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Role;
import com.ms.tracking_api.handlers.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoService.class);

    public Map<String, String> sendMessageToAll(String title, String body) {
        List<User> usersInvalidos = new ArrayList<>();

        List<User> users = userService.findAll()
                .stream()
                .filter(user -> {
                    if (user.getRole() == Role.ADMIN) {
                        return false;
                    }
                    if (!isUsuarioValido(user)) {
                        usersInvalidos.add(user);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Map<String, String> resultados = new LinkedHashMap<>();

        // Lista para armazenar falhas no envio de notificações
        List<String> falhas = new ArrayList<>();

        // Se não houver usuários válidos, retorna uma mensagem apropriada
        if (users.isEmpty()) {
            resultados.put("Mensagem", "Falha ao enviar notificação: usuários sem perfil adequado ou sem token de notificação.");

            if (!usersInvalidos.isEmpty()) {
                resultados.put("Usuários inválidos", formatarUsuariosInvalidos(usersInvalidos));
            }
            return resultados;
        }

        users.forEach(user -> {
            try {
                enviarNotificacao(user, title, body);
            } catch (FirebaseMessagingException e) {
                falhas.add(formatarMensagemErro(user, e.getMessage()));
            }
        });

        StringBuilder mensagem = new StringBuilder("Todas as notificações foram enviadas com sucesso.");

        if (!falhas.isEmpty()) {
            mensagem.append(" No entanto, algumas falharam.");
        }

        if (!usersInvalidos.isEmpty()) {
            mensagem.append(" Alguns usuários não atenderam aos requisitos.");
        }

        resultados.put("Mensagem", mensagem.toString());

        if (!falhas.isEmpty()) {
            resultados.put("Erros", String.join("; ", falhas));
        }

        if (!usersInvalidos.isEmpty()) {
            resultados.put("Usuários inválidos", "Falha ao enviar notificação: alguns usuários não possuem perfil adequado ou estão sem token de notificação.");
            resultados.put("Detalhes", formatarUsuariosInvalidos(usersInvalidos));
        }

        return resultados;
    }

    private String formatarUsuariosInvalidos(List<User> usersInvalidos) {
        return usersInvalidos.stream()
                .map(user -> user.getNome() + " - " + user.getEmail())
                .collect(Collectors.joining("; "));
    }

    public Map<String, String> sendNotificationToUser(Long idUsuario, String title, String body) {
        User user = userService.findById(idUsuario);

        if (!isUsuarioValido(user)) {
            throw new BadRequestException("Falha ao enviar notificação para " + user.getNome() +
                    " (Email: " + user.getEmail() + "): usuário sem perfil adequado ou sem token de notificação.");
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


    public boolean sendMessage(String recipientToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(recipientToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Mensagem enviada com sucesso: {}", response);
            return true;
        } catch (FirebaseMessagingException e) {
            if (e.getMessage().contains("401")) {
                logger.error("Erro de autenticação: verifique as credenciais do Firebase. {}", e.getMessage());
            } else {
                logger.error("Erro ao enviar mensagem FCM: {}", e.getMessage(), e);
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar a notificação: {}", e.getMessage(), e);
            return false;
        }
    }
}




