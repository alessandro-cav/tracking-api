package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Funcionario;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Long>, JpaRepository<Funcionario, Long>{


    Optional<Funcionario> findByCpf(String cpf);

    List<Funcionario> findByNomeContainingIgnoreCase(String nome, PageRequest pageRequest);
}
