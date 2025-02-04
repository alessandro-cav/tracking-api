package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ComprovanteRequest;
import com.ms.tracking_api.dtos.responses.ComprovanteResponse;
import com.ms.tracking_api.dtos.responses.RegistrarAtividaderResponse;
import com.ms.tracking_api.entities.Comprovante;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.entities.Vaga;
import com.ms.tracking_api.repositories.ComprovanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComprovanteService {


    private final ComprovanteRepository  repository;

    private final VagaService vagaService;

    private final UsuarioService usuarioService;

    @Transactional
    public ComprovanteResponse gerarComprovante(ComprovanteRequest request) {
        Vaga vaga = this.vagaService.buscarVagaPeloId(request.getIdVaga());
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(request.getIdUsuario());
        Comprovante comprovante = Comprovante.builder()
                .usuario(usuario)
                .vaga(vaga)
                .build();
        comprovante =  this.repository.save(comprovante);
        return getComprovanteResponse(comprovante);
    }

    private ComprovanteResponse getComprovanteResponse(Comprovante comprovante) {
        ComprovanteResponse cr = new ComprovanteResponse();
        cr.setNome(comprovante.getUsuario().getNome());
        cr.setDataRecibo(comprovante.getDataCriacao());
        cr.setRg(comprovante.getUsuario().getRg());
        cr.setValor(comprovante.getVaga().getValor());
        cr.setCpf(comprovante.getUsuario().getCpf());
        return cr;
    }

    public List<ComprovanteResponse> gerarComprovanteServico(Long idUsuario) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        return this.repository.findCompranteByUsuarioIdUsuario(usuario.getIdUsuario())
                .stream()
                .map(comp -> getComprovanteResponse(comp))
                .collect(Collectors.toList());
    }
}
