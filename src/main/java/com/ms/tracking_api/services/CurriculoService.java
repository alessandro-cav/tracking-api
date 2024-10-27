package com.ms.tracking_api.services;

import com.ms.tracking_api.entities.Curriculo;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.CurriculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CurriculoService {

    private final CurriculoRepository repository;

    private final UsuarioService usuarioService;

    @Transactional
    public void salvar(Long idUsuario,  String arquivoBase64) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Curriculo curriculo = new Curriculo();
        curriculo.setCurriculo(arquivoBase64);
        curriculo.setUsuario(usuario);
        this.repository.save(curriculo);
    }

    @Transactional(readOnly = true)
    public List<String> buscarTodos(Long idUsuario, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        List<Curriculo> curriculos = this.repository.findAllByUsuarioIdUsuario(usuario.getIdUsuario(), pageRequest);
        return curriculos.stream().map(curriculo -> {
            return curriculo.getCurriculo();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String buscarPeloId(Long idUsuario, Long idCurriculo) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Curriculo curriculo = this.repository.findByUsuarioIdUsuarioAndIdCurriculo(usuario.getIdUsuario(), idCurriculo)
                .orElseThrow(() -> new ObjetoNotFoundException("Arquivo não encontrado!"));
        return curriculo.getCurriculo();
    }

    @Transactional
    public void deletePeloId(Long idUsuario, Long idCurriculo) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        this.repository.findByUsuarioIdUsuarioAndIdCurriculo(usuario.getIdUsuario(), idCurriculo).ifPresentOrElse(curriculo -> {
            try {
                this.repository.delete(curriculo);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Curriculo não pode ser excluido, pois está vinculado em algum Usuario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Curriculo não encontrado!");
        });
    }
}



