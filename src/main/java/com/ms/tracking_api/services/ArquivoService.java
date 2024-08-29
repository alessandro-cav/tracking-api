package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.responses.ContaResponse;
import com.ms.tracking_api.entities.Arquivo;
import com.ms.tracking_api.entities.Conta;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.ArquivoRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository repository;

    private final FuncionarioService funcionarioService;

    @Transactional
    public void salvar(Long idFuncionario, MultipartFile multipartFile) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionario);
        String arquivoBase64 = this.converterArquivoParaBase64(multipartFile);

        String nome = multipartFile.getOriginalFilename();
        String tipo = multipartFile.getContentType();
        long tamanho = multipartFile.getSize();

        Arquivo arquivo = new Arquivo();
        arquivo.setArquivo(arquivoBase64);
        arquivo.setNome(nome);
        arquivo.setTipo(tipo);
        arquivo.setTamanho(tamanho);
        arquivo.setFuncionario(funcionario);

        this.repository.save(arquivo);
    }

    @Transactional(readOnly = true)
    public List<String> buscarTodos(Long idFuncionarios, PageRequest pageRequest) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        List<Arquivo> arquivos = this.repository.findAllByFuncionarioIdFuncionario(funcionario.getIdFuncionario(), pageRequest);
        return arquivos.stream().map(arquivo -> {
            return arquivo.getArquivo();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String buscarPeloId(Long idFuncionarios, Long idArquivo) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Arquivo arquivo = this.repository.findByFuncionarioIdFuncionarioAndIdArquivo(funcionario.getIdFuncionario(), idArquivo)
                .orElseThrow(() -> new ObjetoNotFoundException("Arquivo não encontrado!"));
        return arquivo.getArquivo();
    }

    @Transactional
    public void deletePeloId(Long idFuncionario, Long idArquivo) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionario);
        this.repository.findByFuncionarioIdFuncionarioAndIdArquivo(funcionario.getIdFuncionario(), idArquivo).ifPresentOrElse(arquivo -> {
            try {
                this.repository.delete(arquivo);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Arquivo não pode ser excluido, pois está vinculado em algum funcionario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Arquivo não encontrado!");
        });
    }


    private String converterArquivoParaBase64(MultipartFile arquivo) {
        try {
            // Converter para
            // base64
            byte[] bytes = arquivo.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + arquivo.getOriginalFilename(), e);
        }
    }


    @Transactional
    public org.springframework.core.io.Resource downloadArquivo(Long idFuncionario, Long idArquivo) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionario);
        Arquivo arquivo = this.repository.findById(idArquivo).orElseThrow(() -> new ObjetoNotFoundException("Arquivo não encontrado!"));
        Optional<Arquivo> arquivobase64 = repository.findByFuncionarioIdFuncionarioAndIdArquivo(funcionario.getIdFuncionario(), arquivo.getIdArquivo());

        try {
            // Coloque log para capturar o início do método
            System.out.println("Iniciando o download do arquivo para o funcionário: " + idFuncionario);

            byte[] bytes = Base64.getDecoder().decode(arquivobase64.get().getArquivo());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return new InputStreamResource(inputStream);
        } catch (Exception e) {
            e.printStackTrace(); // Logar o erro completo
            throw new RuntimeException("Erro ao processar o arquivo: " + arquivo, e);
        }
    }

}



