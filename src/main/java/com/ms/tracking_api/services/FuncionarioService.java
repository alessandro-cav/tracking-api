package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.FuncionarioRequest;
import com.ms.tracking_api.dtos.responses.FuncionarioResponse;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.enuns.Genero;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final ModelMapper modelMapper;

    private final FuncionarioRepository repository;

    private final Validator validator;

    @Transactional
    public FuncionarioResponse salvar(FuncionarioRequest funcionarioRequest) {
        this.validator.validaCPF(funcionarioRequest.getCpf());
        this.validator.validaEmail(funcionarioRequest.getEmail());
        this.repository.findByCpf(funcionarioRequest.getCpf()).ifPresent(funcionario -> {
            throw new BadRequestException(funcionarioRequest.getCpf() + " já cadastrado no sistema!");
        });
        this.repository.findByEmail(funcionarioRequest.getEmail()).ifPresent(funcionario -> {
            throw new BadRequestException(funcionarioRequest.getEmail() + " já cadastrado no sistema!");
        });
        Funcionario funcionario = this.modelMapper.map(funcionarioRequest, Funcionario.class);
        funcionario.setGenero(Genero.buscarGenero(funcionarioRequest.getGenero()));
        funcionario = this.repository.save(funcionario);
        return this.modelMapper.map(funcionario, FuncionarioResponse.class);
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(funcionario -> this.modelMapper.map(funcionario, FuncionarioResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(funcionario -> {
            return this.modelMapper.map(funcionario, FuncionarioResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Funcionario não encontrado!"));
    }

    @Transactional
    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(funcionario -> {
            this.repository.delete(funcionario);
        }, () -> {
            throw new ObjetoNotFoundException("Funcionario não encontrado!");
        });
    }

    @Transactional
    public FuncionarioResponse atualizar(Long id, FuncionarioRequest funcionarioRequest) {
        this.validator.validaCPF(funcionarioRequest.getCpf());
        this.validator.validaEmail(funcionarioRequest.getEmail());
        Genero genero =  Genero.buscarGenero(funcionarioRequest.getGenero());
        return this.repository.findById(id).map(funcionario -> {
            if (!(funcionario.getCpf().equals(funcionarioRequest.getCpf()))) {
                this.repository.findByCpf(funcionarioRequest.getCpf()).ifPresent(funcionario1 -> {
                    throw new BadRequestException(funcionarioRequest.getCpf() + " já cadastrado no sistema!");
                });
            }
            if (!(funcionario.getEmail().equals(funcionarioRequest.getEmail()))) {
                this.repository.findByEmail(funcionarioRequest.getEmail()).ifPresent(funcionario1 -> {
                    throw new BadRequestException(funcionarioRequest.getEmail() + " já cadastrado no sistema!");
                });
            }
            funcionarioRequest.setIdFuncionario(funcionario.getIdFuncionario());
            funcionario = this.modelMapper.map(funcionarioRequest, Funcionario.class);
            funcionario.setGenero(genero);
            funcionario = this.repository.save(funcionario);
            return this.modelMapper.map(funcionario, FuncionarioResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Funcionario não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByNomeContainingIgnoreCase(nome, pageRequest).stream()
                .map(funcionario -> modelMapper.map(funcionario, FuncionarioResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Funcionario buscarFuncionarioPeloId(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjetoNotFoundException("Funcionario não encontrado!"));
    }
}
