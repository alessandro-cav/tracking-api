package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.Conta;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends PagingAndSortingRepository<Conta, Long>, JpaRepository<Conta, Long> {


    List<Conta> findAllByUsuarioIdUsuario(Long idUsuario, PageRequest pageRequest);

    Optional<Conta> findByUsuarioIdUsuarioAndIdConta(Long idUsuario, Long idConta);

    List<Conta> findByUsuarioIdUsuarioAndTipoChave(Long idUsuario, String tipoChave, PageRequest pageRequest);
}
