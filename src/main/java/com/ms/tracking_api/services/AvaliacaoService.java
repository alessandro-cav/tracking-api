package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.AvaliacaoRequest;
import com.ms.tracking_api.dtos.responses.AvaliacaoResponse;
import com.ms.tracking_api.entities.Avaliacao;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final UsuarioService usuarioService;

    private final AvaliacaoRepository avaliacaoRepository;

    private final ModelMapper modelMapper;


    @Transactional
    public AvaliacaoResponse salvarAvaliacaoDoUsuario(Long idUsuario, AvaliacaoRequest request) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Avaliacao avaliacao = this.modelMapper.map(request, Avaliacao.class);
        avaliacao.setUsuario(usuario);
        avaliacao = this.avaliacaoRepository.save(avaliacao);
        AvaliacaoResponse avaliacaoResponse = new AvaliacaoResponse();
        avaliacaoResponse.setIdAvalicao(avaliacao.getIdAvalicao());
        avaliacaoResponse.setIdUsuario(avaliacao.getUsuario().getIdUsuario());
        avaliacaoResponse.setEstrela(avaliacao.getEstrela());
        avaliacaoResponse.setComentario(avaliacao.getComentario());
        return avaliacaoResponse;
    }
}
