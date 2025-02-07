package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.CandidaturaRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CandidaturaService {

    private final VagaService vagaService;

    private final  UsuarioService usuarioService;

    private final  EventoService eventoService;

    private final CandidaturaRepository repository;

    private final ModelMapper modelMapper;

    @Transactional
    public void save(CandidaturaRequest request) {
        Evento evento = eventoService.buscarEventoPeloId(request.getIdEvento());
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        validarRelacionamentoEventoVaga(evento, vaga);
        if (vaga.getStatusVaga() == StatusVaga.FECHADA) {
            throw new BadRequestException("Não é possível se candidatar à vaga, pois a mesma está fechada.");
        }
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(vaga.getIdVaga(), usuario.getIdUsuario())
                .ifPresent(fv -> {
                    throw new BadRequestException("Você já se candidatou a esta vaga.");
                });
        Candidatura candidatura = new Candidatura();
        candidatura.setVaga(vaga);
        candidatura.setUsuario(usuario);
        candidatura.setStatusCandidatura(StatusCandidatura.PENDENTE);
        this.repository.save(candidatura);
    }

    @Transactional
    public void excluirPeloUsuarioVagaPeloVagaEUsuario(CandidaturaRequest request) {
        Evento evento = eventoService.buscarEventoPeloId(request.getIdEvento());
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        validarRelacionamentoEventoVaga(evento, vaga);
        this.repository.findByVagaIdVagaAndUsuarioIdUsuario(request.getIdVaga(), request.getIdUsuario()).ifPresentOrElse(fv -> {
            this.repository.delete(fv);
        }, () -> {
            throw new BadRequestException("Dados não encontrado para desvincular");
        });
    }

    @Transactional
    public void aceitarCandidatura(CandidaturaRequest request) {
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
    public void recusarCandidatura(CandidaturaRequest request) {
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
    public CandidaturaResponse buscarPorTipoDeCandidatura(Long idVaga, String statusCandidatura, PageRequest pageRequest) {
        Vaga vaga = vagaService.buscarVagaPeloId(idVaga);
        StatusCandidatura status = StatusCandidatura.buscarStatusCandatura(statusCandidatura);
        List<Candidatura> candidaturas = repository.findByVagaIdVagaAndStatusCandidatura(
                vaga.getIdVaga(), status, pageRequest);
        List<UsuarioCandidatoResponse> usuarios = candidaturas
                .stream()
                .map(c -> gerarUsuarioCandidatoResponse(c))
                .collect(Collectors.toList());

        return CandidaturaResponse.builder().usuarios(usuarios).quantidade(usuarios.size()).build();
    }

    private void validarRelacionamentoEventoVaga(Evento evento, Vaga vaga) {
        if (!vaga.getEvento().getIdEvento().equals(evento.getIdEvento())) {
            throw new BadRequestException("A vaga informada não está associada ao evento especificado.");
        }
    }

    @Transactional(readOnly = true)
    public CandidaturaResponse buscarCandidatosPorVaga(Long idVaga, PageRequest pageRequest) {
        Vaga vaga = vagaService.buscarVagaPeloId(idVaga);
        List<UsuarioCandidatoResponse> ucrs = this.repository.findByVagaIdVaga(vaga.getIdVaga(), pageRequest)
                .stream()
                .map(usuario -> {
                    UsuarioCandidatoResponse response = modelMapper.map(usuario, UsuarioCandidatoResponse.class);
                    response.setEvento(vaga.getEvento().getNome());
                    response.setDescricaoVaga(vaga.getDescricaoVaga());
                    return response;
                })
                .collect(Collectors.toList());
        return CandidaturaResponse.builder().usuarios(ucrs).quantidade(ucrs.size()).build();
    }

    public List<Vaga> findVagasByIdUsuario(Long idUsuario) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        return this.repository.findVagasByUsuarioIdUsuario(usuario.getIdUsuario());
    }

    public Candidatura findByVagaIdVaga(Long idVaga) {
        return this.repository.findByVagaIdVaga(idVaga).get();
    }

    private UsuarioCandidatoResponse gerarUsuarioCandidatoResponse(Candidatura candidatura) {
        return UsuarioCandidatoResponse.builder()
                .idUsuario(candidatura.getUsuario().getIdUsuario())
                .nome(candidatura.getUsuario().getNome())
                .cpf(candidatura.getUsuario().getCpf())
                .rg(candidatura.getUsuario().getRg())
                .email(candidatura.getUsuario().getEmail())
                .telefone(candidatura.getUsuario().getTelefone())
                .imagem(candidatura.getUsuario().getImagem())
                .evento(candidatura.getVaga().getEvento().getNome())
                .descricaoVaga(candidatura.getVaga().getDescricaoVaga())
                .build();
    }


    public List<VagaResponse> buscarVagasCandidatadasPeloIdUsuario(Long idUsuario, PageRequest of) {
        List<Candidatura> candidaturas = this.repository.findByUsuarioIdUsuario(idUsuario, of);
        return candidaturas.stream()
                .map(c -> this.modelMapper.map(c.getVaga(), VagaResponse.class))
                .collect(Collectors.toList());
    }
}


