package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Candidatura;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.entities.Vaga;
import com.ms.tracking_api.enuns.StatusCandidatura;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidaturaRepository extends PagingAndSortingRepository<Candidatura, Long>, JpaRepository<Candidatura, Long> {

    Optional<Candidatura> findByVagaIdVagaAndUsuarioIdUsuario(Long idVaga, Long idUsuario);

    List<Candidatura> findByUsuarioIdUsuario(Long idUsuario);

    List<Candidatura>  findByVagaIdVagaAndStatusCandidatura(Long idVaga, StatusCandidatura statusCandidatura, Pageable pageable);

    List<Candidatura> findByUsuarioIdUsuario(Long idUsuario, Pageable pageable);

    @Query("SELECT c.usuario FROM Candidatura c WHERE c.vaga.idVaga = :idVaga")
    List<Usuario> findByVagaIdVaga(Long idVaga, PageRequest pageRequest);

    @Query("SELECT c.vaga FROM Candidatura c WHERE c.usuario.idUsuario = :idUsuario")
    List<Vaga> findVagasByUsuarioIdUsuario(Long idUsuario, Pageable pageable);
}
