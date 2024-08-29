package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Arquivo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;
import java.util.Optional;

public interface ArquivoRepository extends PagingAndSortingRepository<Arquivo, Long>, JpaRepository<Arquivo, Long> {

    Optional<Arquivo> findByFuncionarioIdFuncionarioAndIdArquivo(Long idFuncionario, Long IdArquivo);

    List<Arquivo> findAllByFuncionarioIdFuncionario(Long idFuncionario, PageRequest pageRequest);
}
