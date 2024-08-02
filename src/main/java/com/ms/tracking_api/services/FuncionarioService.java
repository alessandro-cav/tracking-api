package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.FuncionarioRequest;
import com.ms.tracking_api.dtos.responses.FuncionarioResponse;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.entities.UploadArquivo;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.FuncionarioRepository;
import com.ms.tracking_api.repositories.UploadArquivoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class FuncionarioService {

    private final ModelMapper modelMapper;

    private final FuncionarioRepository repository;

    private final UploadArquivoRepository uploadArquivoRepository;

    private final Validator validator;

    public FuncionarioResponse salvar(FuncionarioRequest funcionarioRequest/*, List<MultipartFile> documentos*/) {
        this.validator.validacaoDoCpfECnpjEEmail(funcionarioRequest.getCpf(), null, null);
        this.repository.findByCpf(funcionarioRequest.getCpf()).ifPresent(funcionario -> {
            throw new BadRequestException(  funcionarioRequest.getCpf() + " já cadastrado no sistema!" );
        });
        Funcionario funcionario = this.modelMapper.map(funcionarioRequest, Funcionario.class);
        funcionario = this.repository.save(funcionario);
       /* if(!(documentos.isEmpty())){
        for (MultipartFile documento : documentos) {
            UploadArquivo uploadArquivo = new UploadArquivo();
            uploadArquivo.setFuncionario(funcionario);
            try {
                uploadArquivo.setDocumento(documento.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            uploadArquivoRepository.save(uploadArquivo);
        }}*/

        return this.modelMapper.map(funcionario, FuncionarioResponse.class);
    }

   public List<FuncionarioResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(funcionario -> this.modelMapper.map(funcionario, FuncionarioResponse.class))
                .collect(Collectors.toList());
    }

    public FuncionarioResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(funcionario -> {
            return this.modelMapper.map(funcionario, FuncionarioResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Funcionario não encontrado!"));
    }

    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(funcionario -> {
                this.repository.delete(funcionario);
        }, () -> {
            throw new ObjetoNotFoundException("Funcionario não encontrado!");
        });
    }

   public FuncionarioResponse atualizar(Long id, FuncionarioRequest funcionarioRequest) {
        this.validator.validacaoDoCpfECnpjEEmail(funcionarioRequest.getCpf(), null, null);
        return this.repository.findById(id).map(funcionario -> {
            if (!(funcionario.getCpf().equals(funcionarioRequest.getCpf()))) {
                this.repository.findByCpf(funcionarioRequest.getCpf()).ifPresent(funcionario1 -> {
                    throw new BadRequestException( funcionarioRequest.getCpf() + " já cadastrado no sistema!" );
                });
            }
            funcionarioRequest.setIdFuncionario(funcionario.getIdFuncionario());
            funcionario = this.modelMapper.map(funcionarioRequest, Funcionario.class);
            funcionario = this.repository.save(funcionario);
            return this.modelMapper.map(funcionario, FuncionarioResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Funcionario não encontrado!"));
    }

    public List<FuncionarioResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByNomeContainingIgnoreCase(nome, pageRequest).stream()
                .map(funcionario -> modelMapper.map(funcionario, FuncionarioResponse.class))
                .collect(Collectors.toList());
    }

    public Funcionario buscarFuncionarioPeloId(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjetoNotFoundException("Funcionario não encontrado!"));
    }
}
