package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Curriculo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculoRepository extends PagingAndSortingRepository<Curriculo, Long>, JpaRepository<Curriculo, Long> {

    Optional<Curriculo> findByUsuarioIdUsuarioAndIdCurriculo(Long idUsuario, Long idCurriculo);

    List<Curriculo> findAllByUsuarioIdUsuario(Long idUsuario, PageRequest pageRequest);
}
