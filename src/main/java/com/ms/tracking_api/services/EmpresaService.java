package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.EmpresaRequest;
import com.ms.tracking_api.dtos.responses.EmpresaResponse;
import com.ms.tracking_api.entities.Empresa;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final ModelMapper modelMapper;

    private final EmpresaRepository repository;

    private final Validator validator;

    @Transactional
    public EmpresaResponse salvar(EmpresaRequest empresaRequest) {
        this.validator.validaCNPJ(empresaRequest.getCnpj());
        this.validator.validaEmail(empresaRequest.getEmail());
        this.repository.findByCnpj(empresaRequest.getCnpj()).ifPresent(empresa -> {
            throw new BadRequestException(empresa.getCnpj() + " já cadastrado no sistema!");
        });
        Empresa empresa = this.modelMapper.map(empresaRequest, Empresa.class);
        empresa = this.repository.save(empresa);
        return this.modelMapper.map(empresa, EmpresaResponse.class);
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(empresa -> this.modelMapper.map(empresa, EmpresaResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpresaResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(empresa -> {
            return this.modelMapper.map(empresa, EmpresaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Empresa não encontrada!"));
    }

    @Transactional
    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(empresa -> {
            try {
                this.repository.delete(empresa);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Empresa não pode ser excluida, pois está vinculada em algum evento");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Empresa não encontrada!");
        });
    }

    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaRequest empresaRequest) {
        this.validator.validaCNPJ(empresaRequest.getCnpj());
        this.validator.validaEmail(empresaRequest.getEmail());
        return this.repository.findById(id).map(empresa -> {
            if (!(empresa.getCnpj().equals(empresaRequest.getCnpj()))) {
                this.repository.findByCnpj(empresaRequest.getCnpj()).ifPresent(empresa1 -> {
                    throw new BadRequestException(empresa1.getCnpj() + " já cadastrado!");
                });
            }
            empresaRequest.setIdEmpresa(empresa.getIdEmpresa());
            empresa = this.modelMapper.map(empresaRequest, Empresa.class);
            empresa = this.repository.save(empresa);
            return this.modelMapper.map(empresa, EmpresaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Empresa não encontrada!"));
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByNomeContainingIgnoreCase(nome, pageRequest).stream()
                .map(empresa -> this.modelMapper.map(empresa, EmpresaResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Empresa buscarEmpresaPeloId(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjetoNotFoundException("Empresa não encontrada!"));
    }
}
