package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.UsuarioVagaRequest;
import com.ms.tracking_api.dtos.responses.EventoResponse;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.enuns.StatusCandidatura;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.UsuarioVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioVagaService {

    private final VagaService vagaService;

    private final UsuarioService usuarioService;

    private final UsuarioVagaRepository repository;

    @Transactional
    public void save(UsuarioVagaRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .ifPresent(fv -> {
                    throw new BadRequestException("Funcionario já esta vinculado a essa vaga");
                });
        UsuarioVaga usuarioVaga = new UsuarioVaga();
        usuarioVaga.setVaga(vaga);
        usuarioVaga.setUsuario(usuario);
        usuarioVaga.setStatusCandidatura(StatusCandidatura.PENDENTE); // criar para aceitar ou recusar
        this.repository.save(usuarioVaga);
    }

    @Transactional
    public void excluirPeloUsuarioVagaPeloVagaEUsuario(UsuarioVagaRequest request) {
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(request.getIdVaga(), request.getIdUsuario()).ifPresentOrElse(fv -> {
            this.repository.delete(fv);
        }, () -> {
            throw new BadRequestException("Dados não encontrado para desvincular");
        });
    }

    @Transactional(readOnly = true)
    public List<UsuarioVaga> findByUsuarioIdUsuario(Long idUsuario) {
        return this.repository.findByUsuarioIdUsuario(idUsuario);
    }

    @Transactional(readOnly = true)
    public UsuarioVaga findByUsuarioIdUsuarioAndVagaIdVaga(Long idUsuario, Long idVaga) {
            return  this.repository.findByVagaIdVagaAndUsuarioIdUsuario(idVaga, idUsuario)
                    .orElseThrow(() -> new BadRequestException("Usuário não vinculado a esta vaga"));
        }

    public void aceitarCandidatura(UsuarioVagaRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());

        UsuarioVaga usuarioVaga = this.repository
                .findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .orElseThrow(() -> new BadRequestException("Candidatura não encontrada para o usuário e vaga informados."));

        if (usuarioVaga.getStatusCandidatura() != StatusCandidatura.PENDENTE) {
            throw new BadRequestException("A candidatura já foi processada. Status atual: " + usuarioVaga.getStatusCandidatura().getDescricao());
        }

        usuarioVaga.setStatusCandidatura(StatusCandidatura.ACEITA);
        this.repository.save(usuarioVaga);
    }

    public void recusarCandidatura(UsuarioVagaRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());

        UsuarioVaga usuarioVaga = this.repository
                .findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .orElseThrow(() -> new BadRequestException("Candidatura não encontrada para o usuário e vaga informados."));

        if (usuarioVaga.getStatusCandidatura() != StatusCandidatura.PENDENTE) {
            throw new BadRequestException("A candidatura já foi processada. Status atual: " + usuarioVaga.getStatusCandidatura().getDescricao());
        }

        usuarioVaga.setStatusCandidatura(StatusCandidatura.RECUSADA);
        this.repository.save(usuarioVaga);
    }

}
