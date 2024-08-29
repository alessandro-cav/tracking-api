package com.ms.tracking_api.configs.email;

import com.ms.tracking_api.configs.jwt.JwtService;
import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserRepository repository;

    private final EnviaEmail email;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncod;

    private final Validator validar;

    public void esqueciMinhaSenha(LoginRequestDTO loginRequestDTO) {
        this.validar.validaEmail(loginRequestDTO.getEmail());
        User user = this.repository.findByEmail(loginRequestDTO.getEmail()).get();
        Map<String, Object> extraClaims = new HashMap<>();
        String token = Jwts.builder().setClaims(extraClaims).setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1800000)).signWith(this.jwtService.getSignInKey(), SignatureAlgorithm.HS256).compact();

        String link = "http://localhost:8080/ms-tracking/users/reset_password?token=" + token;

        email.enviarEmail(user.getEmail(), user.getNome(), link);
    }

    public MensagemResponseDTO resetarSenha(String token, SenhasRequestDTO senhasRequestDTO) {

        try {
            boolean expired = this.jwtService.isTokenExpired(token);

            if (!senhasRequestDTO.getSenha01().equals(senhasRequestDTO.getSenha02())) {
                throw new RuntimeException("Senhas diferentes");
            }
            final String username = this.jwtService.extractUsername(token);
            Optional<User> usuario = this.repository.findByEmail(username);

            String novaSenhaCodificada = passwordEncod.encode(senhasRequestDTO.getSenha01().trim());
            usuario.get().setSenha(novaSenhaCodificada);
            this.repository.saveAndFlush(usuario.get());

            String mensagem = "Senha alterada com sucesso.";
            return MensagemResponseDTO.getMenssagem(mensagem);

        } catch (Exception e) {
            throw new RuntimeException("Token expirado: " + e.getMessage());
        }
    }
}
