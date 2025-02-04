package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Comprovante;
import com.ms.tracking_api.entities.Convite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComprovanteRepository extends PagingAndSortingRepository<Comprovante, Long>, JpaRepository<Comprovante, Long> {


    @Query("SELECT c FROM Comprovante c LEFT JOIN c.usuario WHERE c.usuario.idUsuario = :idUsuario")
    List<Comprovante> findCompranteByUsuarioIdUsuario(@Param("idUsuario") Long idUsuario);
}
