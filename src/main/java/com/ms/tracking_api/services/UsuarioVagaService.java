package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.UsuarioVagaRequest;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.UsuarioVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
                    throw new ObjetoNotFoundException("Funcionario já esta vinculado a essa vaga");
                });
        UsuarioVaga usuarioVaga = new UsuarioVaga();
        usuarioVaga.setVaga(vaga);
        usuarioVaga.setUsuario(usuario);
        this.repository.save(usuarioVaga);
    }

    @Transactional
    public void excluirPeloUsuarioVagaPeloVagaEUsuario(UsuarioVagaRequest request) {
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(request.getIdVaga(), request.getIdUsuario()).ifPresentOrElse(fv -> {
            this.repository.delete(fv);
        }, () -> {
            throw new ObjetoNotFoundException("Não encontrado vincular com vaga e funcionario");
        });
    }

    @Transactional(readOnly = true)
    public List<UsuarioVaga> findByUsuarioIdUsuario(Long idUsuario) {
        return this.repository.findByUsuarioIdUsuario(idUsuario);
    }
}
