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
import com.ms.tracking_api.enuns.TipoAtividade;
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

    private final FuncionarioService funcionarioService;

    private final RegistrarAtividadeRepository repository;

    private final FuncionarioVagaService funcionarioVagaService;

    private final TokenConfig tokenConfig;

    @Transactional(readOnly = true)
    public byte[] gerarQrCode(RegistrarAtividadeRequest request) {
        List<FuncionarioVaga> funcionarioVagas = this.funcionarioVagaService.findByFuncionarioIdFuncionario(request.getIdFuncionario());
        boolean vagaEncontrada = funcionarioVagas.stream()
                .anyMatch(fv -> fv.getVaga().getIdVaga().equals(request.getIdVaga()));

        if (!vagaEncontrada) {
            throw new ObjetoNotFoundException("O funcionário não está vinculado a esta vaga");
        }

        TipoAtividade tipoAtividade = TipoAtividade.buscarTipo(request.getTipoAtividade());
        RegistrarAtividade existeAtividadeRegistrada = this.buscarUltimaAtividadePorFuncionario(request.getIdFuncionario(),
                request.getIdVaga());

        if (existeAtividadeRegistrada != null) {
            if (tipoAtividade.equals(TipoAtividade.SAIDA)) {
                validarIntervaloDeTempo(existeAtividadeRegistrada.getDataHora(), LocalDateTime.now());
            }

            if (existeAtividadeRegistrada.getTipoAtividade().equals(tipoAtividade)) {
                throw new BadRequestException("Funcionario " + existeAtividadeRegistrada.getFuncionario().getNome() +
                        " já possui uma " + existeAtividadeRegistrada.getTipoAtividade() + " cadastrada! ");
            }
        }
        String token = tokenConfig.generateToken(request.getIdFuncionario(), tipoAtividade.getDescricao(), request.getIdVaga());
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

    private RegistrarAtividade buscarUltimaAtividadePorFuncionario(Long idFuncionario, Long idVaga) {
        Optional<RegistrarAtividade> atividades = this.repository.
                findTopByFuncionarioIdFuncionarioAndVagaIdVagaOrderByDataHoraDesc(idFuncionario, idVaga);
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
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(tokenInfo.getIdFuncionario());

        RegistrarAtividade ra = new RegistrarAtividade();
        ra.setTipoAtividade(TipoAtividade.buscarTipo(tokenInfo.getTipoAtividade()));
        ra.setFuncionario(funcionario);
        ra.setVaga(vaga);

        RegistrarAtividaderResponse rar = new RegistrarAtividaderResponse();
        rar.setTipoAtividade(ra.getTipoAtividade());
        rar.setVaga(ra.getVaga().getVaga());
        rar.setNomeFuncinario(ra.getFuncionario().getNome());
        rar.setEvento(ra.getVaga().getEvento().getNome());
        rar.setIdVaga(ra.getVaga().getIdVaga());
        rar.setIdFuncionario(ra.getFuncionario().getIdFuncionario());
        return rar;
    }

    @Transactional
    public String gerarComprovante(RegistrarAtividadeRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(request.getIdFuncionario());

        RegistrarAtividade ra = new RegistrarAtividade();
        ra.setTipoAtividade(TipoAtividade.buscarTipo(request.getTipoAtividade()));
        ra.setFuncionario(funcionario);
        ra.setVaga(vaga);
        ra.setDataHora(LocalDateTime.now());

        this.repository.save(ra);

        // FALTA A CONFIGURAÇÃO PARA ENVIO DE COMPROVANTE VIA WHATSAPP
        return "deu certo ";
    }

}
