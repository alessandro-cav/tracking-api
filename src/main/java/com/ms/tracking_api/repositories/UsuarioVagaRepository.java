package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.UsuarioVaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioVagaRepository extends PagingAndSortingRepository<UsuarioVaga, Long>, JpaRepository<UsuarioVaga, Long> {

    Optional<UsuarioVaga> findByVagaIdVagaAndUsuarioIdUsuario(Long idVaga, Long idUsuario);

    List<UsuarioVaga> findByUsuarioIdUsuario(Long idUsuario);


}
