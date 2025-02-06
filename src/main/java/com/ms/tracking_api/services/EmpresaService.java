package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.EmpresaRequest;
import com.ms.tracking_api.dtos.responses.EmpresaResponse;
import com.ms.tracking_api.entities.Empresa;
import com.ms.tracking_api.entities.Endereco;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.enuns.Status;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.validator.validaCNPJ(empresaRequest.getCnpj());
        this.validator.validaEmail(empresaRequest.getEmail());
        this.repository.findByCnpjBancoAndCnpj(user.getCnpjBanco(),empresaRequest.getCnpj()).ifPresent(empresa -> {
            throw new BadRequestException(empresa.getCnpj() + " já cadastrado no sistema!");
        });
        this.repository.findByCnpjAndEmail(user.getCnpjBanco(),empresaRequest.getEmail()).ifPresent(funcionario1 -> {
            throw new BadRequestException(empresaRequest.getEmail() + " já cadastrado no sistema!");
        });
        Empresa empresa = this.modelMapper.map(empresaRequest, Empresa.class);
        empresa.setEndereco(getEndereco(empresa, empresaRequest));
        empresa.setCnpjBanco(user.getCnpjBanco());
        empresa = this.repository.save(empresa);
        return   gerarEmpresaResponse(empresa);
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> buscarTodos(PageRequest pageRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBanco(user.getCnpjBanco(), pageRequest).stream()
                .map(empresa ->  gerarEmpresaResponse(empresa))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpresaResponse buscarPeloId(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjAndIdEmpresa(user.getCnpjBanco(),id).map(empresa -> {
            return   gerarEmpresaResponse(empresa);
        }).orElseThrow(() -> new ObjetoNotFoundException("Empresa não encontrada!"));
    }

    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaRequest empresaRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(empresaRequest.getCnpj() != null) {
            this.validator.validaCNPJ(empresaRequest.getCnpj());
        }
        if(empresaRequest.getEmail() != null) {
            this.validator.validaEmail(empresaRequest.getEmail());
        }
        return this.repository.findByCnpjAndIdEmpresa(user.getCnpjBanco(),id).map(empresa -> {
            if(empresaRequest.getCnpj() != null) {
                if (!(empresa.getCnpj().equals(empresaRequest.getCnpj()))) {
                    this.repository.findByCnpjBancoAndCnpj(user.getCnpjBanco(),empresaRequest.getCnpj()).ifPresent(empresa1 -> {
                        throw new BadRequestException(empresa1.getCnpj() + " já cadastrado!");
                    });
                }
            }
            if(empresaRequest.getEmail() != null) {
                if (!(empresa.getEmail().equals(empresaRequest.getEmail()))) {
                    this.repository.findByCnpjAndEmail(user.getCnpjBanco(),empresaRequest.getEmail()).ifPresent(funcionario1 -> {
                        throw new BadRequestException(empresaRequest.getEmail() + " já cadastrado no sistema!");
                    });
                }
            }
            empresa = this.atualizarEmpresa(empresa, empresaRequest);
            empresa.setEndereco(getEndereco(empresa, empresaRequest));
            empresa.setCnpjBanco(user.getCnpjBanco());
            empresa = this.repository.save(empresa);
            return  gerarEmpresaResponse(empresa);
        }).orElseThrow(() -> new ObjetoNotFoundException("Empresa não encontrada!"));
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBancoAndNomeContainingIgnoreCase(user.getCnpjBanco(),nome, pageRequest).stream()
                .map(empresa ->  gerarEmpresaResponse(empresa))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Empresa buscarEmpresaPeloId(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return this.repository.findByCnpjAndIdEmpresa(user.getCnpjBanco(),id).orElseThrow(() -> new ObjetoNotFoundException("Empresa não encontrada!"));
    }

    @Transactional
    public void inativarEmpresa(Long idEmpresa) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Empresa empresa =  this.repository.findByCnpjAndIdEmpresa(user.getCnpjBanco(), idEmpresa).get();
        if (empresa.getStatus() == Status.INATIVO) {
            throw new BadRequestException(" A empresa já está com o status INATIVO.");
        }
        empresa.setStatus(Status.INATIVO);
       this.repository.save(empresa);
    }

    @Transactional
    public void ativarEmpresa(Long idEmpresa) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Empresa empresa =  this.repository.findByCnpjAndIdEmpresa(user.getCnpjBanco(), idEmpresa).get();
        if (empresa.getStatus() == Status.ATIVO) {
            throw new BadRequestException("A empresa já está com o status ATIVO.");
        }
        user.setStatus(Status.ATIVO);
        this.repository.save(empresa);
    }

    private Endereco getEndereco(Empresa empresa, EmpresaRequest empresaRequest) {
        Endereco endereco = empresa.getEndereco() != null ? empresa.getEndereco() : new Endereco();
        endereco.setLogradouro(empresaRequest.getLogradouro() == null ? endereco.getLogradouro() : empresaRequest.getLogradouro());
        endereco.setNumero(empresaRequest.getNumero() == null ? endereco.getNumero() : empresaRequest.getNumero());
        endereco.setEstado(empresaRequest.getEstado() == null ? endereco.getEstado() : empresaRequest.getEstado());
        endereco.setCidade(empresaRequest.getCidade() == null ? endereco.getCidade() : empresaRequest.getCidade());
        endereco.setBairro(empresaRequest.getBairro() == null ? endereco.getBairro() : empresaRequest.getBairro());
        endereco.setCep(empresaRequest.getCep() == null ? endereco.getCep() : empresaRequest.getCep());
        return endereco;
    }

    public Empresa atualizarEmpresa(Empresa empresa, EmpresaRequest empresaRequest) {
        empresa.setNome(empresaRequest.getNome() == null ? empresa.getNome() : empresaRequest.getNome());
        empresa.setCnpj(empresaRequest.getCnpj() == null ? empresa.getCnpj() : empresaRequest.getCnpj());
        empresa.setTelefone(empresaRequest.getTelefone() == null ? empresa.getTelefone() : empresaRequest.getTelefone());
        empresa.setEmail(empresaRequest.getEmail() == null ? empresa.getEmail() : empresaRequest.getEmail());
        empresa.setImagem(empresaRequest.getImagem() == null ? empresa.getImagem() : empresaRequest.getImagem());
        return empresa;
    }

    private EmpresaResponse gerarEmpresaResponse(Empresa empresa) {
        EmpresaResponse empresaResponse = this.modelMapper.map(empresa, EmpresaResponse.class);
        empresaResponse.setLogradouro(empresa.getEndereco().getLogradouro());
        empresaResponse.setNumero(empresa.getEndereco().getNumero());
        empresaResponse.setEstado(empresa.getEndereco().getEstado());
        empresaResponse.setCidade(empresa.getEndereco().getCidade());
        empresaResponse.setBairro(empresa.getEndereco().getBairro());
        empresaResponse.setCep(empresa.getEndereco().getCep());
        return  empresaResponse;
    }
}
