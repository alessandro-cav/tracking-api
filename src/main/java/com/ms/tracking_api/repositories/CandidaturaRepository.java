package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Candidatura;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidaturaRepository extends PagingAndSortingRepository<Candidatura, Long>, JpaRepository<Candidatura, Long> {

    Optional<Candidatura> findByVagaIdVagaAndUsuarioIdUsuario(Long idVaga, Long idUsuario);

    List<Candidatura> findByUsuarioIdUsuario(Long idUsuario);

    List<Candidatura>  findByVagaIdVagaAndStatusCandidatura(Long idVaga, String statusCandidatura, Pageable pageable);

    List<Candidatura> findByUsuarioIdUsuario(Long idUsuario, Pageable pageable);

    List<Candidatura> findByVagaIdVaga(Long idVaga, PageRequest pageRequest);
}
