package com.ms.tracking_api.configs.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@RequiredArgsConstructor
public class EnviaEmail {

    private final JavaMailSender javaMailSender;

    public void emailAlterarSenha(String destino, String nome, String link) {
        try {
            String titulo = "Redefinição de Senha";

            String conteudo = "Olá " + nome + ", Houve uma solicitação para alterar sua senha de email! \n\n "
                    + "Se você não fez essa solicitação, ignore este e-mail."
                    + "Caso contrário, clique neste link para alterar sua senha: \n\n" + "Link: " + link;

            extracted(destino, titulo, conteudo);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    public void emailConvite(String destino, String nome, String codigo) {
        try {
            String titulo = "Convite para acesso ao aplicativo";

            String texto = "Olá " + nome + ",\n\n" +
                    "Estamos enviando este código de acesso: " + codigo +
                    ". Utilize-o junto com o e-mail " + destino +
                    " para acessar o aplicativo.\n\n" +
                    "Se precisar de ajuda, entre em contato conosco.\n\n";
            
            extracted(destino, titulo, texto);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    private void extracted(String destino, String titulo, String conteudo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destino);
        mensagem.setSubject(titulo);
        mensagem.setText(conteudo);

        javaMailSender.send(mensagem);
    }
}
