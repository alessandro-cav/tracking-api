package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ReciboRequest;
import com.ms.tracking_api.dtos.requests.QRCodeRequest;
import com.ms.tracking_api.dtos.responses.ReciboResponse;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.enuns.TipoAcesso;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.RegistrarAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrarAtividadeService {

    private final VagaService vagaService;

    private final UsuarioService usuarioService;

    private final EventoService eventoService;

    private final RegistrarAtividadeRepository repository;

    private final UsuarioVagaService usuarioVagaService;


    @Transactional(readOnly = true)
    public RegistrarAtividaderResponse registrarAtividade(QRCodeRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        Evento evento = this.eventoService.buscarEventoPeloId(request.getIdEvento());

        UsuarioVaga usuarioVaga = this.usuarioVagaService.findByUsuarioIdUsuarioAndVagaIdVaga(usuario.getIdUsuario(), vaga.getIdVaga());

        if (!usuarioVaga.getVaga().getEvento().getIdEvento().equals(evento.getIdEvento())) {
            throw new BadRequestException("A vaga que o usuário está vinculado não pertence a esse evento");
        }

        RegistrarAtividade atividade =  buscarUltimaAtividadePorUsuario(usuario.getIdUsuario(), vaga.getIdVaga());

        if( atividade  == null ){
            atividade = new RegistrarAtividade();
            atividade.setTipoAcesso(TipoAcesso.ENTRADA);
        }else {
            validarIntervaloDeTempo(atividade.getDataHora());
            atividade.setTipoAcesso(TipoAcesso.SAIDA);
        }
        atividade.setUsuario(usuario);
        atividade.setVaga(vaga);

        atividade = this.repository.save(atividade);

        RegistrarAtividaderResponse atividaderResponse = new RegistrarAtividaderResponse();
        atividaderResponse.setIdRegistrarAtividade(atividade.getIdRegistrarAtividade());
        atividaderResponse.setNome(atividade.getUsuario().getNome());
        atividaderResponse.setVaga(atividade.getVaga().getVaga());
        atividaderResponse.setTipoAcesso(atividade.getTipoAcesso());
        atividaderResponse.setEvento(atividade.getVaga().getEvento().getNome());
        return atividaderResponse;
    }

    /*private void validarIntervaloDeTempo(LocalDateTime ultimaAtividade) {
        if (ultimaAtividade != null) {
            long horasDeDiferenca = Duration.between(ultimaAtividade, LocalDateTime.now()).toHoursPart();

            if (horasDeDiferenca < 2) {
                throw new BadRequestException("O registro de saída deve ser gerado pelo menos 2 horas após o inicio do evento.");
            }
        }
    }*/

    private void validarIntervaloDeTempo(LocalDateTime ultimaAtividade) {
        if (ultimaAtividade != null) {
            long minutosDeDiferenca = Duration.between(ultimaAtividade, LocalDateTime.now()).toMinutes();

            // Verifica se a diferença é menor que 5 minutos
            if (minutosDeDiferenca < 5) {
                throw new BadRequestException("O registro de saída deve ser gerado pelo menos 5 minutos após o inicio do evento.");
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
        return cr;
    }

}
