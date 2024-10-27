package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.Endereco;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends PagingAndSortingRepository<Endereco, Long>, JpaRepository<Endereco, Long> {

    List<Endereco> findAllByEventoIdEvento(Long idEventos, PageRequest pageRequest);

   Optional<Endereco> findByEventoIdEventoAndIdEndereco(Long idEventos, Long idEndereco);

    List<Endereco> findByEventoIdEventoAndLogradouroContainingIgnoreCase(Long idEventos, String nome, PageRequest pageRequest);

    List<Endereco> findAllByUsuarioIdUsuario(Long idUsuario, PageRequest pageRequest);

    Optional<Endereco> findByUsuarioIdUsuarioAndIdEndereco(Long idUsuario, Long idEndereco);

    List<Endereco> findByUsuarioIdUsuarioAndLogradouroContainingIgnoreCase(Long idUsuario, String logradouro, PageRequest pageRequest);
}
