package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.Endereco;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends PagingAndSortingRepository<Endereco, Long>, JpaRepository<Endereco, Long> {

    List<Endereco> findAllByEventoIdEvento(Long idEventos, PageRequest pageRequest);

   Optional<Endereco> findByEventoIdEventoAndIdEndereco(Long idEventos, Long idEndereco);

    List<Endereco> findByEventoIdEventoAndLogradouroContainingIgnoreCase(Long idEventos, String nome, PageRequest pageRequest);

    Optional<Endereco> findByFuncionarioIdFuncionarioAndIdEndereco(Long idFuncionario, Long idEndereco);

    List<Endereco> findAllByFuncionarioIdFuncionario(Long idFuncionario, PageRequest pageRequest);

    List<Endereco> findByFuncionarioIdFuncionarioAndLogradouroContainingIgnoreCase(Long idFuncionario, String logradouro, PageRequest pageRequest);
}
