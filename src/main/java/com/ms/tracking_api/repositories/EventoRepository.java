package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Empresa;
import com.ms.tracking_api.entities.Evento;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends PagingAndSortingRepository<Evento, Long>, JpaRepository<Evento, Long>{


    List<Evento> findByNomeContainingIgnoreCase(String nome, PageRequest pageRequest);
}
