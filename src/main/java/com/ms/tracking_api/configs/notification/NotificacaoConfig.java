package com.ms.tracking_api.configs.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class NotificacaoConfig {


    @Bean
    public FirebaseMessaging firebaseApp() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("notificacao.json");
        if (serviceAccount == null) {
            throw new IllegalArgumentException("SArquivo da conta de serviço não encontrado nos recursos, notificacao.json");
        }
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        return FirebaseMessaging.getInstance(app);
    }
}
