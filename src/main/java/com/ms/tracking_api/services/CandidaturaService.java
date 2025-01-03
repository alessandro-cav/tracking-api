package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.UsuarioVagaRequest;
import com.ms.tracking_api.dtos.responses.CandidaturaResponse;
import com.ms.tracking_api.dtos.responses.UsuarioCandidatoResponse;
import com.ms.tracking_api.dtos.responses.VagaResponse;
import com.ms.tracking_api.entities.Candidatura;
import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.entities.Vaga;
import com.ms.tracking_api.enuns.StatusCandidatura;
import com.ms.tracking_api.enuns.StatusVaga;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.CandidaturaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CandidaturaService {

    private final @Lazy VagaService vagaService;

    private final  @Lazy UsuarioService usuarioService;

    private final  @Lazy EventoService eventoService;

    private final CandidaturaRepository repository;

    @Transactional
    public void save(UsuarioVagaRequest request) {
        Evento evento = eventoService.buscarEventoPeloId(request.getIdEvento());
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        validarRelacionamentoEventoVaga(evento, vaga);
        if (vaga.getStatusVaga() == StatusVaga.FECHADA) {
            throw new BadRequestException("Não é possível se candidatar à vaga, pois a mesma está fechada.");
        }
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .ifPresent(fv -> {
                    throw new BadRequestException("Usuário já está vinculado a esta vaga.");
                });
        Candidatura candidatura = new Candidatura();
        candidatura.setVaga(vaga);
        candidatura.setUsuario(usuario);
        candidatura.setStatusCandidatura(StatusCandidatura.PENDENTE);
        this.repository.save(candidatura);
    }

    @Transactional
    public void excluirPeloUsuarioVagaPeloVagaEUsuario(UsuarioVagaRequest request) {
        Evento evento = eventoService.buscarEventoPeloId(request.getIdEvento());
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        validarRelacionamentoEventoVaga(evento, vaga);
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(request.getIdVaga(), request.getIdUsuario()).ifPresentOrElse(fv -> {
            this.repository.delete(fv);
        }, () -> {
            throw new BadRequestException("Dados não encontrado para desvincular");
        });
    }

    @Transactional(readOnly = true)
    public List<Candidatura> findByUsuarioIdUsuario(Long idUsuario) {
        return this.repository.findByUsuarioIdUsuario(idUsuario);
    }

    @Transactional(readOnly = true)
    public Candidatura findByUsuarioIdUsuarioAndVagaIdVaga(Long idUsuario, Long idVaga) {
        return this.repository.findByVagaIdVagaAndUsuarioIdUsuario(idVaga, idUsuario)
                .orElseThrow(() -> new BadRequestException("Usuário não vinculado a esta vaga"));
    }

    @Transactional
    public void aceitarCandidatura(UsuarioVagaRequest request) {
        Evento evento = eventoService.buscarEventoPeloId(request.getIdEvento());
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        validarRelacionamentoEventoVaga(evento, vaga);

        Candidatura candidatura = this.repository
                .findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .orElseThrow(() -> new BadRequestException("Candidatura não encontrada para o usuário e vaga informados."));

        if (candidatura.getStatusCandidatura() != StatusCandidatura.PENDENTE) {
            throw new BadRequestException("A candidatura já foi processada. Status atual: " + candidatura.getStatusCandidatura().getDescricao() + ".");
        }

        candidatura.setStatusCandidatura(StatusCandidatura.ACEITA);
        this.repository.save(candidatura);
    }

    @Transactional
    public void recusarCandidatura(UsuarioVagaRequest request) {
        Evento evento = eventoService.buscarEventoPeloId(request.getIdEvento());
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        validarRelacionamentoEventoVaga(evento, vaga);

        Candidatura candidatura = this.repository
                .findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .orElseThrow(() -> new BadRequestException("Candidatura não encontrada para o usuário e vaga informados."));

        if (candidatura.getStatusCandidatura() != StatusCandidatura.PENDENTE) {
            throw new BadRequestException("A candidatura já foi processada. Status atual: " + candidatura.getStatusCandidatura().getDescricao() + ".");
        }

        candidatura.setStatusCandidatura(StatusCandidatura.RECUSADA);
        this.repository.save(candidatura);
    }

    // ver como vai funcionar a concluida

    @Transactional(readOnly = true)
    public CandidaturaResponse buscar(Long idEvento, Long idVaga, String statusCandidatura, PageRequest pageRequest) {
        Evento evento = eventoService.buscarEventoPeloId(idEvento);
        Vaga vaga = vagaService.buscarVagaPeloId(idVaga);
        validarRelacionamentoEventoVaga(evento, vaga);
        List<Candidatura> candidaturas = repository.findByVagaIdVagaAndStatusCandidatura(
                vaga.getIdVaga(), statusCandidatura, pageRequest);

        List<UsuarioCandidatoResponse> usuarios = candidaturas
                .stream()
                .map(c -> gerarUsuarioCandidatoResponse(c.getUsuario()))
                .collect(Collectors.toList());

        return CandidaturaResponse.builder().usuarios(usuarios).quantidade(usuarios.size()).build();
    }

    private void validarRelacionamentoEventoVaga(Evento evento, Vaga vaga) {
        if (!vaga.getEvento().getIdEvento().equals(evento.getIdEvento())) {
            throw new BadRequestException("A vaga informada não está associada ao evento especificado.");
        }
    }

    private UsuarioCandidatoResponse gerarUsuarioCandidatoResponse(Usuario usuario) {
        return UsuarioCandidatoResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .rg(usuario.getRg())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .imagem(usuario.getImagem())
                .build();
    }

    @Transactional(readOnly = true)
    public List<Candidatura> findByUsuarioIdUsuario(Long idUsuario, PageRequest pageRequest) {
        return this.repository.findByUsuarioIdUsuario(idUsuario, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Candidatura> findByVagaIdVaga(Long idVaga, PageRequest pageRequest) {
        return this.repository.findByVagaIdVaga(idVaga, pageRequest);
    }
}


