package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.UsuarioRequest;
import com.ms.tracking_api.dtos.responses.UsuarioResponse;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.enuns.Genero;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final ModelMapper modelMapper;

    private final UsuarioRepository repository;

    private final Validator validator;

    @Transactional
    public UsuarioResponse salvar(UsuarioRequest usuarioRequest) {
        this.validator.validaCPF(usuarioRequest.getCpf());
        this.validator.validaEmail(usuarioRequest.getEmail());
        this.repository.findByCpf(usuarioRequest.getCpf()).ifPresent(funcionario -> {
            throw new BadRequestException(usuarioRequest.getCpf() + " já cadastrado no sistema!");
        });
        this.repository.findByEmail(usuarioRequest.getEmail()).ifPresent(funcionario -> {
            throw new BadRequestException(usuarioRequest.getEmail() + " já cadastrado no sistema!");
        });
        Usuario usuario = this.modelMapper.map(usuarioRequest, Usuario.class);
        usuario.setGenero(Genero.buscarGenero(usuarioRequest.getGenero()));
        usuario = this.repository.save(usuario);
        return this.modelMapper.map(usuario, UsuarioResponse.class);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(usuario -> this.modelMapper.map(usuario, UsuarioResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(usuario -> {
            return this.modelMapper.map(usuario, UsuarioResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Usuário não encontrado!"));
    }

    @Transactional
    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(funcionario -> {
            this.repository.delete(funcionario);
        }, () -> {
            throw new ObjetoNotFoundException("Usuário não encontrado!");
        });
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest usuarioRequest) {
        this.validator.validaCPF(usuarioRequest.getCpf());
        this.validator.validaEmail(usuarioRequest.getEmail());
        Genero genero =  Genero.buscarGenero(usuarioRequest.getGenero());
        return this.repository.findById(id).map(usuario -> {
            if (!(usuario.getCpf().equals(usuarioRequest.getCpf()))) {
                this.repository.findByCpf(usuarioRequest.getCpf()).ifPresent(funcionario1 -> {
                    throw new BadRequestException(usuarioRequest.getCpf() + " já cadastrado no sistema!");
                });
            }
            if (!(usuario.getEmail().equals(usuarioRequest.getEmail()))) {
                this.repository.findByEmail(usuarioRequest.getEmail()).ifPresent(funcionario1 -> {
                    throw new BadRequestException(usuarioRequest.getEmail() + " já cadastrado no sistema!");
                });
            }
            usuarioRequest.setIdUsuario(usuario.getIdUsuario());
            usuario = this.modelMapper.map(usuarioRequest, Usuario.class);
            usuario.setGenero(genero);
            usuario = this.repository.save(usuario);
            return this.modelMapper.map(usuario, UsuarioResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Usuário não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByNomeContainingIgnoreCase(nome, pageRequest).stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioPeloId(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjetoNotFoundException("Usuário não encontrado!"));
    }
}
