package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.RegistrarAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrarAtividadeRepository extends PagingAndSortingRepository<RegistrarAtividade, Long>, JpaRepository<RegistrarAtividade, Long>{



        Optional<RegistrarAtividade> findTopByUsuarioIdUsuarioAndVagaIdVagaOrderByDataHoraDesc(Long idUsuario, Long idVaga);
}
