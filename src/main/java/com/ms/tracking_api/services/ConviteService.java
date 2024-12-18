package com.ms.tracking_api.services;

import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.responses.ConviteResponse;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Role;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConviteService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final Validator validator;

    public ConviteResponse criarUsuarioConvidado(ConviteRequest request) {
        this.validator.validaEmail(request.getEmail());
        this.repository.findByEmail(request.getEmail()).ifPresent(email -> {
            throw new BadRequestException
                    ("O e-mail: " + request.getEmail() + " já foi utilizado para convidar um usuário." );
        });
        User user = User.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode("123456"))
                .role(Role.USUARIO)
                .build();
        user = repository.save(user);

        return ConviteResponse.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .build();
    }
}
