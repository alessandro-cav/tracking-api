package com.ms.tracking_api.services;

import com.ms.tracking_api.configs.email.EnviaEmail;
import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.ConviteDTO;
import com.ms.tracking_api.dtos.requests.ConviteRequest;
import com.ms.tracking_api.dtos.requests.FiltroConviteRequest;
import com.ms.tracking_api.dtos.requests.ValidarConviteRequest;
import com.ms.tracking_api.dtos.responses.ConviteResponse;
import com.ms.tracking_api.dtos.responses.MensagemResponse;
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
    public MensagemResponse enviarConvite(ConviteRequest request) {
        List<String> emailsInvalidos = new ArrayList<>();
        List<String> emailsJaUtilizados = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        List<ConviteDTO> convitesValidos = request.getConvites().stream()
                .filter(conviteDTO -> {
                    if (!validator.isEmailValid(conviteDTO.getEmail())) {
                        emailsInvalidos.add(conviteDTO.getEmail());
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        convitesValidos.forEach(conviteDTO -> {
            if (repository.findByEmail(conviteDTO.getEmail()).isPresent()) {
                emailsJaUtilizados.add(conviteDTO.getEmail());
            } else {
                String codigo = generateUniqueCode();
                Convite convite = Convite.builder()
                        .email(conviteDTO.getEmail())
                        .nome(conviteDTO.getNome())
                        .codigo(codigo)
                        .statusConvite(StatusConvite.PENDENTE)
                        .build();

               convite =  repository.save(convite);
                enviarEmailConvite(convite);
            }
        });

        if (!emailsInvalidos.isEmpty())
            erros.add("Os seguintes e-mails são inválidos: " + String.join(", ", emailsInvalidos));

        if (!emailsJaUtilizados.isEmpty())
            erros.add("Os seguintes e-mails já foram utilizados para convidar um usuário: "
                    + String.join(", ", emailsJaUtilizados));

        if (!erros.isEmpty())
             return new MensagemResponse("Houve erros ao processar os convites: " + erros);

        return new MensagemResponse("Convites enviados com sucesso.");
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

    public MensagemResponse reenviarConvite(ConviteRequest request) {
        List<String> emailsInvalidos = new ArrayList<>();
        List<String> emailsComStatusDiferenteDePendente = new ArrayList<>();
        List<String> emailsNaoEncontrados = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        List<ConviteDTO> convitesValidos = request.getConvites().stream()
                .filter(conviteDTO -> {
                    if (!validator.isEmailValid(conviteDTO.getEmail())) {
                        emailsInvalidos.add(conviteDTO.getEmail());
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        convitesValidos.forEach(conviteDTO -> {
            repository.findByEmailAndNome(conviteDTO.getEmail(), conviteDTO.getNome())
                    .ifPresentOrElse(convite -> {
                        if (convite.getStatusConvite() == StatusConvite.VALIDADO) {
                            emailsComStatusDiferenteDePendente.add(conviteDTO.getEmail());
                        } else {
                            enviarEmailConvite(convite);
                        }
                    }, () -> emailsNaoEncontrados.add(conviteDTO.getEmail()));
        });

        if (!emailsInvalidos.isEmpty())
            erros.add("Os seguintes e-mails são inválidos: " + String.join(", ", emailsInvalidos));

        if (!emailsNaoEncontrados.isEmpty())
            erros.add("Convite não encontrado para os seguintes e-mails: " + String.join(", ", emailsNaoEncontrados));

        if (!emailsComStatusDiferenteDePendente.isEmpty())
            erros.add("Os seguintes e-mails já foram utilizados para acesso à aplicação e não podem receber um novo convite: "
                    + String.join(", ", emailsComStatusDiferenteDePendente));

        if (!erros.isEmpty())
            return new MensagemResponse("Houve erros ao processar os convites: " + erros);

        return new MensagemResponse("Convites enviados com sucesso.");
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
