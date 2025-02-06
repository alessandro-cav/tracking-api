package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Vaga;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends PagingAndSortingRepository<Evento, Long>, JpaRepository<Evento, Long>{


    List<Evento> findByCnpjBancpAndNomeContainingIgnoreCase(String nome, String s, PageRequest pageRequest);

    List<Evento> findByCnpjBanco(String cnpjBanco, PageRequest pageRequest);

    Optional<Evento> findByCnpjBancoAndIdEvento(String cnpjBanco, Long id);

    @Query("SELECT v FROM Vaga v LEFT JOIN v.evento e WHERE e.idEvento = :idEvento AND v.cnpjBanco = :cnpjBanco")
    List<Vaga> findVagasByCnpjBancoAndIdEvento(String cnpjBanco, Long idEvento);

}
