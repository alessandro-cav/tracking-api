package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.RegistrarAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrarAtividadeRepository extends PagingAndSortingRepository<RegistrarAtividade, Long>, JpaRepository<RegistrarAtividade, Long>{

        Optional<RegistrarAtividade> findTopByUsuarioIdUsuarioAndVagaIdVagaOrderByDataHoraDesc(Long idUsuario, Long idVaga);

        @Query("SELECT r FROM RegistrarAtividade r LEFT JOIN r.usuario WHERE r.usuario.idUsuario = :idUsuario")
        List<RegistrarAtividade> findRegistrarAtividadeByUsuarioIdUsuario(@Param("idUsuario") Long idUsuario);


}
