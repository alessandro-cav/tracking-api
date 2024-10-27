package com.ms.tracking_api.services;


import com.ms.tracking_api.entities.Empresa;
import com.ms.tracking_api.entities.Imagem;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.ImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ImagemService {

    private final ImagemRepository repository;

    private final UsuarioService usuarioService;

    private  final EmpresaService empresaService;

    @Transactional
    public void salvar(Long idUsuario,  String arquivoBase64) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Imagem imagem = new Imagem();
        imagem.setImagem(arquivoBase64);
        imagem.setUsuario(usuario);

        this.repository.save(imagem);
    }

    @Transactional
    public void salvarEmpresa(Long idEmpresa,  String arquivoBase64) {
      Empresa empresa = this.empresaService.buscarEmpresaPeloId(idEmpresa);
        Imagem imagem = new Imagem();
        imagem.setImagem(arquivoBase64);
        imagem.setEmpresa(empresa);
        this.repository.save(imagem);
    }


    @Transactional(readOnly = true)
    public List<String> buscarTodos(Long idUsuario, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        List<Imagem> imagems = this.repository.findAllByUsuarioIdUsuario(usuario.getIdUsuario(), pageRequest);
        return imagems.stream().map(imagem -> {
            return imagem.getImagem();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> buscarTodosEmpresa(Long idEmpresa, PageRequest pageRequest) {
        Empresa empresa = this.empresaService.buscarEmpresaPeloId(idEmpresa);
        List<Imagem> imagems = this.repository.findAllByEmpresaIdEmpresa(empresa.getIdEmpresa(), pageRequest);
        return imagems.stream().map(imagem -> {
            return imagem.getImagem();
        }).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public String buscarPeloId(Long idUsuario, Long idImagem) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Imagem imagem = this.repository.findByUsuarioIdUsuarioAndIdImagem(usuario.getIdUsuario(), idImagem)
                .orElseThrow(() -> new ObjetoNotFoundException("Imagem não encontrado!"));
        return imagem.getImagem();
    }

    @Transactional(readOnly = true)
    public String buscarPeloIdEmpresa(Long idEmpresa, Long idImagem) {
        Empresa empresa = this.empresaService.buscarEmpresaPeloId(idEmpresa);
        Imagem imagem = this.repository.findByEmpresaIdEmpresaAndIdImagem(empresa.getIdEmpresa(), idImagem)
                .orElseThrow(() -> new ObjetoNotFoundException("Imagem não encontrado!"));
        return imagem.getImagem();
    }


    @Transactional
    public void deletePeloId(Long idUsuario, Long idImagem) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        this.repository.findByUsuarioIdUsuarioAndIdImagem(usuario.getIdUsuario(), idImagem).ifPresentOrElse(imagem -> {
            try {
                this.repository.delete(imagem);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Imagem não pode ser excluido, pois está vinculado em algum usuario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Imagem não encontrada!");
        });
    }

    @Transactional
    public void deletePeloIdEmpresa(Long idEmpresa, Long idImagem) {
        Empresa empresa = this.empresaService.buscarEmpresaPeloId(idEmpresa);
        this.repository.findByEmpresaIdEmpresaAndIdImagem(empresa.getIdEmpresa(), idImagem).ifPresentOrElse(imagem -> {
            try {
                this.repository.delete(imagem);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Imagem não pode ser excluido, pois está vinculado em algum empresa");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Imagem não encontrada!");
        });
    }
}



