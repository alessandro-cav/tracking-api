package com.ms.tracking_api.services;

import com.ms.tracking_api.entities.Aso;
import com.ms.tracking_api.entities.Curriculo;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.AsoRepository;
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
public class AsoService {

    private final AsoRepository repository;

    private final UsuarioService usuarioService;

    @Transactional
    public void salvar(Long idUsuario,  String arquivoBase64) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Aso aso = new Aso();
        aso.setAso(arquivoBase64);
        aso.setUsuario(usuario);
        this.repository.save(aso);
    }

    @Transactional(readOnly = true)
    public List<String> buscarTodos(Long idUsuario, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        List<Aso> asos = this.repository.findAllByUsuarioIdUsuario(usuario.getIdUsuario(), pageRequest);
        return asos.stream().map(aso -> {
            return aso.getAso();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String buscarPeloId(Long idUsuario, Long idAso) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Aso aso = this.repository.findByUsuarioIdUsuarioAndIdAso(usuario.getIdUsuario(), idAso)
                .orElseThrow(() -> new ObjetoNotFoundException("ASO não encontrado!"));
        return aso.getAso();
    }

    @Transactional
    public void deletePeloId(Long idUsuario, Long idAso) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        this.repository.findByUsuarioIdUsuarioAndIdAso(usuario.getIdUsuario(), idAso).ifPresentOrElse(aso -> {
            try {
                this.repository.delete(aso);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("ASO não pode ser excluido, pois está vinculado em algum Usuario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("ASO não encontrado!");
        });
    }
}



