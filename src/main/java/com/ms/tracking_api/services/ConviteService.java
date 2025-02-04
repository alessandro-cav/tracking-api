package com.ms.tracking_api.services;

import com.ms.tracking_api.configs.email.EnviaEmail;
import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.requests.FiltroConviteRequest;
import com.ms.tracking_api.dtos.requests.ValidarConviteRequest;
import com.ms.tracking_api.dtos.responses.ConviteResponse;
import com.ms.tracking_api.entities.Convite;
import com.ms.tracking_api.enuns.StatusConvite;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.ConviteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConviteService {

    private final ConviteRepository repository;

    private final Validator validator;

    private final EnviaEmail email;

    private final ModelMapper modelMapper;

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
    }
    @Transactional
    public void enviarConvite(ConviteRequest request) {
        List<String> emailsInvalidos = new ArrayList<>();
        List<String> emailsJaUtilizados = new ArrayList<>();
        List<String> todosErros = new ArrayList<>();

        List<String> emailsValidos = request.getEmails().stream()
                .filter(email -> {
                    if (!this.validator.isEmailValid(email)) {
                        emailsInvalidos.add(email);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        emailsValidos.forEach(email -> {
            if (this.repository.findByEmail(email).isPresent()) {
                emailsJaUtilizados.add(email);
            } else {
                String codigo = this.generateUniqueCode();
                Convite convite = Convite.builder()
                        .email(email)
                        .nome(request.getNome())
                        .codigo(codigo)
                        .statusConvite(StatusConvite.PENDENTE)
                        .build();
                this.repository.save(convite);
                this.enviarEmailConvite(convite);
            }
        });

        if (!emailsInvalidos.isEmpty()) {
            todosErros.add("Os seguintes e-mails são inválidos: " + String.join(", ", emailsInvalidos));
        }
        if (!emailsJaUtilizados.isEmpty()) {
            todosErros.add("Os seguintes e-mails já foram utilizados para convidar um usuário: " + String.join(", ", emailsJaUtilizados));
        }
        if (!todosErros.isEmpty()) {
            throw new BadRequestException(String.join("\n", todosErros));
        }
    }

    @Transactional
    public boolean validarConvite(ValidarConviteRequest request) {
        this.validator.validaEmail(request.getEmail());
        return this.repository.findByEmailAndCodigo(request.getEmail(), request.getCodigo())
                .map(convite -> {
                    convite.setStatusConvite(StatusConvite.VALIDADO);
                    this.repository.save(convite);
                    return true;
                })
                .orElse(false);
    }

    public Boolean reenviarConvite(ConviteRequest request) {
        List<String> emailsInvalidos = new ArrayList<>();
        List<String> emailsComStatusDiferenteDePendente = new ArrayList<>();
        List<String> emailsNaoEncontrados = new ArrayList<>();
        List<String> todosErros = new ArrayList<>();

        // Filtra e-mails inválidos
        List<String> emailsValidos = request.getEmails().stream()
                .filter(email -> {
                    if (!this.validator.isEmailValid(email)) {
                        emailsInvalidos.add(email);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        emailsValidos.forEach(email -> {
            this.repository.findByEmailAndNome(email, request.getNome())
                    .ifPresentOrElse(convite -> {
                        if (convite.getStatusConvite() == StatusConvite.VALIDADO) {
                            emailsComStatusDiferenteDePendente.add(email);
                        } else {
                            this.enviarEmailConvite(convite);
                        }
                    }, () -> emailsNaoEncontrados.add(email));
        });

        if (!emailsInvalidos.isEmpty()) {
            todosErros.add("Os seguintes e-mails são inválidos: " + String.join(", ", emailsInvalidos));
        }
        if (!emailsNaoEncontrados.isEmpty()) {
            todosErros.add("Convite não encontrado para os seguintes e-mails: " + String.join(", ", emailsNaoEncontrados));
        }
        if (!emailsComStatusDiferenteDePendente.isEmpty()) {
            todosErros.add("Os seguintes e-mails já foram utilizados para acesso à aplicação e não podem receber um novo convite: "
                    + String.join(", ", emailsComStatusDiferenteDePendente));
        }
        if (!todosErros.isEmpty()) {
            throw new BadRequestException(String.join("\n", todosErros));
        }
        return true;
    }


    private void enviarEmailConvite(Convite convite) {
        email.emailConvite(convite.getEmail(), convite.getNome(), convite.getCodigo());
    }

    public List<ConviteResponse> filtroConvite(FiltroConviteRequest filtroConviteRequestDTO,
                                               PageRequest pageRequest) {
        Convite convite = this.modelMapper.map(filtroConviteRequestDTO, Convite.class);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Convite> example = Example.of(convite, exampleMatcher);

        Page<Convite> convites = this.repository.findAll(example, pageRequest);
        return convites.stream().map(conv -> {
            return this.modelMapper.map(conv, ConviteResponse.class);
        }).collect(Collectors.toList());
    }

    public List<ConviteResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(conv -> this.modelMapper.map(conv, ConviteResponse.class))
                .collect(Collectors.toList());
    }
}
