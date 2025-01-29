package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ReciboRequest;
import com.ms.tracking_api.dtos.requests.QRCodeRequest;
import com.ms.tracking_api.dtos.responses.ReciboResponse;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.enuns.StatusCandidatura;
import com.ms.tracking_api.enuns.TipoAcesso;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.RegistrarAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RegistrarAtividadeService {

    private final VagaService vagaService;

    private final UsuarioService usuarioService;

    private final EventoService eventoService;

    private final RegistrarAtividadeRepository repository;

    private final CandidaturaService candidaturaService;

    @Transactional
    public RegistrarAtividaderResponse registrarAtividade(QRCodeRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        Evento evento = this.eventoService.buscarEventoPeloId(request.getIdEvento());

        Candidatura candidatura = buscarUsuarioVaga(usuario, vaga);
        validarUsuarioVaga(candidatura, evento);

        RegistrarAtividade atividade = registrarEntradaOUSaida(usuario, vaga);

        atividade = this.repository.save(atividade);
        return gerarRespostaAtividade(atividade);
    }


    private Candidatura buscarUsuarioVaga(Usuario usuario, Vaga vaga) {
        return this.candidaturaService.findByUsuarioIdUsuarioAndVagaIdVaga(usuario.getIdUsuario(), vaga.getIdVaga());
    }

    private void validarUsuarioVaga(Candidatura candidatura, Evento evento) {
        if (!candidatura.getVaga().getEvento().getIdEvento().equals(evento.getIdEvento())) {
            throw new BadRequestException("A vaga que o usuário está vinculado não pertence a esse evento");
        }

        if (candidatura.getStatusCandidatura() == StatusCandidatura.PENDENTE || candidatura.getStatusCandidatura() == StatusCandidatura.RECUSADA) {
            throw new BadRequestException("A candidatura de trabalho está pendente ou foi recusada para este evento, portanto, não é possível realizar a entrada.");
        }
    }

    private RegistrarAtividade registrarEntradaOUSaida(Usuario usuario, Vaga vaga) {
        RegistrarAtividade atividade = buscarUltimaAtividadePorUsuario(usuario.getIdUsuario(), vaga.getIdVaga());

        if (atividade == null) {
            return registrarEntrada(usuario, vaga);
        } else {
          validarIntervaloDeTempo(atividade.getDataHora());
            return registrarSaida(usuario, vaga);
        }
    }

    private RegistrarAtividade registrarEntrada(Usuario usuario, Vaga vaga) {
        RegistrarAtividade atividade = new RegistrarAtividade();
        atividade.setTipoAcesso(TipoAcesso.ENTRADA);
        atividade.setUsuario(usuario);
        atividade.setVaga(vaga);
        return atividade;
    }

    private RegistrarAtividade registrarSaida(Usuario usuario, Vaga vaga) {
        RegistrarAtividade atividade = new RegistrarAtividade();
        atividade.setTipoAcesso(TipoAcesso.SAIDA);
        atividade.setUsuario(usuario);
        atividade.setVaga(vaga);
        return atividade;
    }

    private RegistrarAtividaderResponse gerarRespostaAtividade(RegistrarAtividade atividade) {
        RegistrarAtividaderResponse response = new RegistrarAtividaderResponse();
        response.setIdRegistrarAtividade(atividade.getIdRegistrarAtividade());
        response.setNome(atividade.getUsuario().getNome());
        response.setDescricaoVaga(atividade.getVaga().getDescricaoVaga());
        response.setTipoAcesso(atividade.getTipoAcesso());
        response.setEvento(atividade.getVaga().getEvento().getNome());
        return response;
    }

    private void validarIntervaloDeTempo(LocalDateTime ultimaAtividade) {
        if (ultimaAtividade != null) {
            long horasDeDiferenca = Duration.between(ultimaAtividade, LocalDateTime.now()).toHoursPart();

            if (horasDeDiferenca < 2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String horaFormatada = ultimaAtividade.format(formatter);
                throw new BadRequestException("O registro de saída deve ser gerado no mínimo 2 horas após o início do evento. Registro de horário de entrada: " + horaFormatada);
            }
        }
    }

    @Transactional(readOnly = true)
    private RegistrarAtividade buscarUltimaAtividadePorUsuario(Long idUsuario, Long idVaga) {
        return this.repository.
                findTopByUsuarioIdUsuarioAndVagaIdVagaOrderByDataHoraDesc(idUsuario, idVaga).orElse(null);
    }

    @Transactional(readOnly = true)
    public ReciboResponse gerarComprovante(ReciboRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());

        ReciboResponse cr = new ReciboResponse();
        cr.setNome(usuario.getNome());
        cr.setRg(usuario.getRg());
        cr.setValor(vaga.getValor());
        cr.setCpf(usuario.getCpf());
        return cr;
    }

}
