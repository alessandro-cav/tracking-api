package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Imagem;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagemRepository extends PagingAndSortingRepository<Imagem, Long>, JpaRepository<Imagem, Long> {

    Optional<Imagem> findByUsuarioIdUsuarioAndIdImagem(Long idUsuario, Long idImagem);

    List<Imagem> findAllByUsuarioIdUsuario(Long idUsuario, PageRequest pageRequest);

    List<Imagem> findAllByEmpresaIdEmpresa(Long idEmpresa, PageRequest pageRequest);

    Optional<Imagem> findByEmpresaIdEmpresaAndIdImagem(Long idEmpresa, Long idImagem);
}
