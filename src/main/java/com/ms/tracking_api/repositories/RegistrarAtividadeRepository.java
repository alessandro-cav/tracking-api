package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.RegistrarAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RegistrarAtividadeRepository extends PagingAndSortingRepository<RegistrarAtividade, Long>, JpaRepository<RegistrarAtividade, Long>{



        Optional<RegistrarAtividade> findTopByFuncionarioIdFuncionarioAndVagaIdVagaOrderByDataHoraDesc(Long idFuncionario, Long idVaga);
}
