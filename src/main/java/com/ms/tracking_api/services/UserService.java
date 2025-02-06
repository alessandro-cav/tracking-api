package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.FiltroUsuarioRequest;
import com.ms.tracking_api.dtos.responses.UserResponse;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Status;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<UserResponse> buscarTodosWeb(PageRequest pageRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBanco(user.getCnpjBanco(), pageRequest).stream()
                .map(user1 -> this.modelMapper.map(user1, UserResponse.class))
                .collect(Collectors.toList());
    }

    public List<UserResponse> filtroUsuarioWeb(FiltroUsuarioRequest filtroUsuarioRequest,
                                               PageRequest pageRequest) {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = this.modelMapper.map(filtroUsuarioRequest, User.class);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<User> example = Example.of(user, exampleMatcher);

        Page<User> users = this.repository.findByCnpjBanco(example, pageRequest, userLogado.getCnpjBanco());
        return users.stream().map(user1 -> {
            return this.modelMapper.map(user1, UserResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void inativarUser(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user1 = this.repository.findByCnpjBancoAndId(user.getCnpjBanco(), id).get();
        if (user1.getStatus() == Status.INATIVO) {
            throw new BadRequestException("O usuário já está com o status INATIVO.");
        }
        user1.setStatus(Status.INATIVO);
        this.repository.save(user1);
    }


    @Transactional
    public void ativarUser(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user1 = this.repository.findByCnpjBancoAndId(user.getCnpjBanco(), id).get();
        if (user1.getStatus() == Status.ATIVO) {
            throw new BadRequestException("O usuário já está com o status ATIVO.");
        }
        user1.setStatus(Status.ATIVO);
        this.repository.save(user1);
    }

}
