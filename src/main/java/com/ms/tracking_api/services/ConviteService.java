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

    @Transactional
    public void enviarConvite(ConviteRequest request) {
        this.validator.validaEmail(request.getEmail());
        this.repository.findByEmail(request.getEmail()).ifPresent(email -> {
            throw new BadRequestException(
                    "O e-mail: " + request.getEmail() + " já foi utilizado para convidar um usuário."
            );
        });
        String codigo = this.generateUniqueCode();
        Convite convite = Convite.
                builder()
                .email(request.getEmail())
                .nome(request.getNome())
                .codigo(codigo)
                .statusConvite(StatusConvite.PENDENTE)
                .build();

        convite = this.repository.save(convite);
        this.enviarEmailConvite(convite);
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
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
        return this.repository.findByEmailAndNome(request.getEmail(), request.getNome())
                .map(convite -> {
                   this.validarStatusConvite(convite);
                    this.enviarEmailConvite(convite);
                    return true;
                })
                .orElseThrow(() -> new BadRequestException("Convite não encontrado para os dados fornecidos."));
    }

    private void validarStatusConvite(Convite convite) {
        if (convite.getStatusConvite() == StatusConvite.VALIDADO) {
            throw new BadRequestException(
                    "Não é possível reenviar o convite, pois o acesso ao aplicativo já foi validado com sucesso!");
        }
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
