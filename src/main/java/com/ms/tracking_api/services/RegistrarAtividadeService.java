package com.ms.tracking_api.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ms.tracking_api.configs.tokenConfig.TokenConfig;
import com.ms.tracking_api.dtos.requests.RegistrarAtividadeRequest;
import com.ms.tracking_api.dtos.requests.TokenRequest;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
import com.ms.tracking_api.dtos.responses.TokenInfo;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.enuns.TipoAcesso;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ErrorGeneratingTokenException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.RegistrarAtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrarAtividadeService {

    private final VagaService vagaService;

    private final UsuarioService usuarioService;

    private final RegistrarAtividadeRepository repository;

    private final UsuarioVagaService usuarioVagaService;

    private final TokenConfig tokenConfig;

    @Transactional(readOnly = true)
    public byte[] gerarQrCode(RegistrarAtividadeRequest request) {
        List<UsuarioVaga> usuarioVagases = this.usuarioVagaService.findByUsuarioIdUsuario(request.getIdUsuario());
        boolean vagaEncontrada = usuarioVagases.stream()
                .anyMatch(fv -> fv.getVaga().getIdVaga().equals(request.getIdVaga()));

        if (!vagaEncontrada) {
            throw new ObjetoNotFoundException("O funcionário não está vinculado a esta vaga");
        }

        TipoAcesso tipoAcesso = TipoAcesso.buscarTipo(request.getTipoAcesso());
        RegistrarAtividade existeAtividadeRegistrada = this.buscarUltimaAtividadePorUsuario(request.getIdUsuario(),
                request.getIdVaga());

        if (existeAtividadeRegistrada != null) {
            if (tipoAcesso.equals(TipoAcesso.SAIDA)) {
                validarIntervaloDeTempo(existeAtividadeRegistrada.getDataHora(), LocalDateTime.now());
            }

            if (existeAtividadeRegistrada.getTipoAcesso().equals(tipoAcesso)) {
                throw new BadRequestException("Funcionario " + existeAtividadeRegistrada.getUsuario().getNome() +
                        " já possui uma " + existeAtividadeRegistrada.getTipoAcesso() + " cadastrada! ");
            }
        }
        String token = tokenConfig.generateToken(request.getIdUsuario(), tipoAcesso.getDescricao(), request.getIdVaga());
        String link = String.format("http://localhost:8080/registrarAtividades/registrarAtividade?token=%s", token);
        byte[] qrCodeImage = null;
        try {
            qrCodeImage = this.generateQRCodeImage(link, 200, 200);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "image/png");
        } catch (WriterException | IOException e) {
            throw new ErrorGeneratingTokenException("Erro ao gerar o qrcode " + e.getMessage());
        }
        return qrCodeImage;
    }

    private void validarIntervaloDeTempo(LocalDateTime ultimaAtividade, LocalDateTime dataHoraAtual) {
        if (ultimaAtividade != null && Duration.between(ultimaAtividade, dataHoraAtual).toHours() < 2) {
            throw new BadRequestException("O qrcode de saída deve ser gerado pelo menos 2 horas após o inicio do evento.");
        }
    }

    private RegistrarAtividade buscarUltimaAtividadePorUsuario(Long idFuncionario, Long idVaga) {
        Optional<RegistrarAtividade> atividades = this.repository.
                findTopByUsuarioIdUsuarioAndVagaIdVagaOrderByDataHoraDesc(idFuncionario, idVaga);
        return atividades.isEmpty() ? null : atividades.get();
    }

    private byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @Transactional(readOnly = true)
    public RegistrarAtividaderResponse registrarAtividade(TokenRequest request) {
        TokenInfo tokenInfo = this.tokenConfig.validateToken(request.getToken());
        Vaga vaga = this.vagaService.buscarVagaPeloId(tokenInfo.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(tokenInfo.getIdFuncionario());

        RegistrarAtividade ra = new RegistrarAtividade();
        ra.setTipoAcesso(TipoAcesso.buscarTipo(tokenInfo.getTipoAtividade()));
        ra.setUsuario(usuario);
        ra.setVaga(vaga);

        RegistrarAtividaderResponse rar = new RegistrarAtividaderResponse();
        rar.setTipoAcesso(ra.getTipoAcesso());
        rar.setVaga(ra.getVaga().getVaga());
        rar.setNomeFuncinario(ra.getUsuario().getNome());
        rar.setEvento(ra.getVaga().getEvento().getNome());
        rar.setIdVaga(ra.getVaga().getIdVaga());
        rar.setIdUsuario(ra.getUsuario().getIdUsuario());
        return rar;
    }

    @Transactional
    public String gerarComprovante(RegistrarAtividadeRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());

        RegistrarAtividade ra = new RegistrarAtividade();
        ra.setTipoAcesso(TipoAcesso.buscarTipo(request.getTipoAcesso()));
        ra.setUsuario(usuario);
        ra.setVaga(vaga);
        ra.setDataHora(LocalDateTime.now());

        this.repository.save(ra);

        // FALTA A CONFIGURAÇÃO PARA ENVIO DE COMPROVANTE VIA WHATSAPP
        return "deu certo ";
    }

}
