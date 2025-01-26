package com.ms.tracking_api.services;

import com.ms.tracking_api.configs.email.EnviaEmail;
import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.requests.ValidarConviteRequest;
import com.ms.tracking_api.entities.Convite;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.ConviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConviteService {

    private final ConviteRepository repository;

    private final Validator validator;

    private final EnviaEmail email;

    @Transactional
    public void enviarConvite(ConviteRequest request) {
        this.validator.validaEmail(request.getEmail());
        this.repository.findByEmail(request.getEmail()).ifPresent(email -> {
            throw new BadRequestException(
                    "O e-mail: " + request.getEmail() + " já foi utilizado para convidar um usuário."
            );
        });
        String codigo = this.generateUniqueCode();
        Convite convite =  Convite.
                builder()
                .email(request.getEmail())
                .nome(request.getNome())
                .codigo(codigo)
                .build();

        convite = this.repository.save(convite);
        email.emailConvite(convite.getEmail(), convite.getNome(), convite.getCodigo());
    }

    private String generateUniqueCode() {
       return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
    }

    public boolean validarConvite(ValidarConviteRequest request) {
        this.validator.validaEmail(request.getEmail());
        return this.repository.findByEmailAndCodigo(request.getEmail(), request.getCodigo()).isPresent();
    }

}
