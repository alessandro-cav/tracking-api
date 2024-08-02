package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Vaga;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VagaRepository extends PagingAndSortingRepository<Vaga, Long>, JpaRepository<Vaga, Long>{

    List<Vaga> findByVagaContainingIgnoreCase(String nome, PageRequest pageRequest);
}
