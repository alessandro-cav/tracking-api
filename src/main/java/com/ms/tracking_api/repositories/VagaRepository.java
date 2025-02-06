package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Vaga;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface VagaRepository extends PagingAndSortingRepository<Vaga, Long>, JpaRepository<Vaga, Long>{

    List<Vaga> findByDescricaoVagaContainingIgnoreCase(String nome, PageRequest pageRequest);

    List<Vaga> findByCnpjBanco(String cnpjBanco, PageRequest pageRequest);

    Optional<Vaga> findByCnpjBancoAndIdVaga(String cnpjBanco, Long id);

    List<Vaga> findByCnpjBancoAndDescricaoVagaContainingIgnoreCase(String nome, PageRequest pageRequest);
}
