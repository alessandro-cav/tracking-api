package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ContaRequest;
import com.ms.tracking_api.dtos.responses.ContaResponse;;
import com.ms.tracking_api.entities.Conta;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.enuns.Pix;
import com.ms.tracking_api.enuns.TipoConta;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.ContaRepository;
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
public class ContaService {

    private final FuncionarioService funcionarioService;

    private final ContaRepository contaRepository;

    private final ModelMapper modelMapper;


    @Transactional
    public ContaResponse salvarContaDoFuncionario(Long idFuncionarios, ContaRequest request) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Pix pix = Pix.buscarChavePix(request.getPix());
        Conta conta = this.modelMapper.map(request, Conta.class);
        conta.setFuncionario(funcionario);
        conta.setPix(pix);
        conta = this.contaRepository.save(conta);
        return this.modelMapper.map(conta, ContaResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarTodasContaDoFuncionario(Long idFuncionarios, PageRequest pageRequest) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        List<Conta> contas = this.contaRepository.findAllByFuncionarioIdFuncionario(funcionario.getIdFuncionario(), pageRequest);
        return contas.stream().map(conta -> {
            return this.modelMapper.map(conta, ContaResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarContaDoFuncionarioPeloId(Long idFuncionarios, Long idConta) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Conta conta =  this.contaRepository.findByFuncionarioIdFuncionarioAndIdConta(funcionario.getIdFuncionario(), idConta)
                .orElseThrow(() -> new ObjetoNotFoundException("Conta não encontrada!"));
        return this.modelMapper.map(conta, ContaResponse.class);
    }

    @Transactional
    public void deleteContaDoFuncionario(Long idFuncionarios, Long idConta) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        this.contaRepository.findByFuncionarioIdFuncionarioAndIdConta(funcionario.getIdFuncionario(), idConta).ifPresentOrElse(conta -> {
            try {
                this.contaRepository.delete(conta);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Conta não pode ser excluida, pois está vinculada em algum funcionario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Conta não encontrado!");
        });
    }

    @Transactional
    public ContaResponse atualizarContaDoFuncionario(Long idFuncionarios, Long idConta, ContaRequest request) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Pix pix = Pix.buscarChavePix(request.getPix());
        return  this.contaRepository.findByFuncionarioIdFuncionarioAndIdConta(funcionario.getIdFuncionario(), idConta).map(conta -> {
            request.setIdConta(conta.getIdConta());
            conta = this.modelMapper.map(request, Conta.class);
            conta.setFuncionario(funcionario);
            conta.setPix(pix);
            conta = this.contaRepository.save(conta);
            return this.modelMapper.map(conta, ContaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Conta não encontrada!"));
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarContaDoFuncionarioPorTipoConta(Long idFuncionarios, String tipoPix, PageRequest pageRequest) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Pix pix = Pix.buscarChavePix(tipoPix);
        return this.contaRepository.findByFuncionarioIdFuncionarioAndPix(funcionario.getIdFuncionario(), pix, pageRequest).stream()
                .map(conta -> this.modelMapper.map(conta, ContaResponse.class))
                .collect(Collectors.toList());
    }
}
