package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ComprovanteRequest;
import com.ms.tracking_api.dtos.requests.QRCodeRequest;
import com.ms.tracking_api.dtos.responses.ComprovanteResponse;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.enuns.StatusCandidatura;
import com.ms.tracking_api.enuns.TipoAcesso;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.repositories.RegistrarAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrarAtividadeService {

    private final ModelMapper modelMapper;

    private final UsuarioService usuarioService;

    private final RegistrarAtividadeRepository repository;

    private final CandidaturaService candidaturaService;

    @Transactional
    public String registrarAtividade(QRCodeRequest request) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        List<Vaga> vagas = this.candidaturaService.findVagasByIdUsuario(usuario.getIdUsuario());

        RegistrarAtividade atividade = vagas.stream()
                .filter(vaga -> vaga.getEvento().getIdEvento().equals(request.getIdEvento()))
                .findFirst()
                .map(vaga -> {
                    this.validarVagaAprovada(vaga);
                    RegistrarAtividade novaAtividade = registrarEntradaOUSaida(usuario, vaga);
                    return this.repository.save(novaAtividade);
                })
                .orElseThrow(() -> new RuntimeException("Nenhuma vaga encontrada para o evento informado"));

        return atividade == null
                ? "Ocorreu um erro ao registrar o acesso. Por favor, tente novamente. Se o problema persistir, entre em contato com o setor responsável."
                : atividade.getTipoAcesso() + " registrada com sucesso!";

    }

    private void validarVagaAprovada(Vaga vaga) {
         Candidatura candidatura = this.candidaturaService.findByVagaIdVaga(vaga.getIdVaga());
        if (candidatura.getStatusCandidatura() == StatusCandidatura.PENDENTE || candidatura.getStatusCandidatura() == StatusCandidatura.RECUSADA) {
            throw new BadRequestException("A candidatura de trabalho está pendente ou foi recusada para este evento, portanto, não é possível realizar a entrada.");
        }
    }

    private RegistrarAtividade registrarEntradaOUSaida(Usuario usuario, Vaga vaga) {
        RegistrarAtividade atividade = buscarUltimaAtividadePorUsuario(usuario.getIdUsuario(), vaga.getIdVaga());

        if (atividade == null) {
            return registrarEntrada(usuario, vaga);
        } else {
         // validarIntervaloDeTempo(atividade.getDataHora());
            return registrarSaida(usuario, vaga);
        }
    }

    private RegistrarAtividade registrarEntrada(Usuario usuario, Vaga vaga) {
        RegistrarAtividade atividade = new RegistrarAtividade();
        atividade.setTipoAcesso(TipoAcesso.ENTRADA);
        atividade.setUsuario(usuario);
        atividade.setVaga(vaga);
        atividade.setDataHora(LocalDateTime.now());
        return atividade;
    }

    private RegistrarAtividade registrarSaida(Usuario usuario, Vaga vaga) {
        RegistrarAtividade atividade = new RegistrarAtividade();
        atividade.setTipoAcesso(TipoAcesso.SAIDA);
        atividade.setUsuario(usuario);
        atividade.setVaga(vaga);
        atividade.setHoraSaida(LocalDateTime.now());
        return atividade;
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

    public List<RegistrarAtividaderResponse> listarRegistroAtividades(Long idUsuario) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        return this.repository.findRegistrarAtividadeByUsuarioIdUsuario(usuario.getIdUsuario())
                .stream()
                .map(ra -> this.modelMapper.map(ra, RegistrarAtividaderResponse.class))
                .collect(Collectors.toList());
    }

}
