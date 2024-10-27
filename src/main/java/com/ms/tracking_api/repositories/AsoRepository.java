package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Aso;
import com.ms.tracking_api.entities.Curriculo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsoRepository extends PagingAndSortingRepository<Aso, Long>, JpaRepository<Aso, Long> {

    Optional<Aso> findByUsuarioIdUsuarioAndIdAso(Long idUsuario, Long idAso);

    List<Aso> findAllByUsuarioIdUsuario(Long idUsuario, PageRequest pageRequest);
}
