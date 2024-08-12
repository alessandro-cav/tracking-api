package com.ms.tracking_api.services;

import com.ms.tracking_api.configs.tokenConfig.TokenConfig;
import com.ms.tracking_api.dtos.requests.FuncionarioVagaRequest;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.FuncionarioVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FuncionarioVagaService {

    private final VagaService vagaService;

    private final FuncionarioService funcionarioService;

    private final FuncionarioVagaRepository repository;

    @Transactional
    public void save(FuncionarioVagaRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(request.getIdFuncionario());
        this.repository.findByVagaIdVagaAndFuncionarioIdFuncionario(request.getIdVaga(), request.getIdFuncionario())
                .ifPresent(fv -> {
                    throw new ObjetoNotFoundException("Funcionario já esta vinculado a essa vaga");
                });
        FuncionarioVaga funcionarioVaga = new FuncionarioVaga();
        funcionarioVaga.setVaga(vaga);
        funcionarioVaga.setFuncionario(funcionario);
        this.repository.save(funcionarioVaga);
    }

    @Transactional
    public void excluirPeloAlunoMateiraPeloAlunoEMateria(FuncionarioVagaRequest request) {
        this.repository.findByVagaIdVagaAndFuncionarioIdFuncionario(request.getIdVaga(), request.getIdFuncionario()).ifPresentOrElse(fv -> {
            this.repository.delete(fv);
        }, () -> {
            throw new ObjetoNotFoundException("Não encontrado vincular com vaga e funcionario");
        });
    }

    @Transactional(readOnly = true)
    public List<FuncionarioVaga> findByFuncionarioIdFuncionario(Long idFuncionario) {
        return this.repository.findByFuncionarioIdFuncionario(idFuncionario);
    }
}
