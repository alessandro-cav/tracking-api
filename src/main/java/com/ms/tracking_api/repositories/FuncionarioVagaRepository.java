package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.entities.FuncionarioVaga;
import com.ms.tracking_api.entities.RegistrarAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioVagaRepository extends PagingAndSortingRepository<FuncionarioVaga, Long>, JpaRepository<FuncionarioVaga, Long> {
    Optional<FuncionarioVaga> findByVagaIdVagaAndFuncionarioIdFuncionario(Long idVaga, Long idFuncionario);

    List<FuncionarioVaga> findByFuncionarioIdFuncionario(Long idFuncionario);

}
