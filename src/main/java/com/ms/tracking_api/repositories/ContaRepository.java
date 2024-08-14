package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.Conta;
import com.ms.tracking_api.entities.Endereco;
import com.ms.tracking_api.enuns.Pix;
import com.ms.tracking_api.enuns.TipoConta;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends PagingAndSortingRepository<Conta, Long>, JpaRepository<Conta, Long> {

    Optional<Conta> findByFuncionarioIdFuncionarioAndIdConta(Long idFuncionario, Long idEndereco);

    List<Conta> findAllByFuncionarioIdFuncionario(Long idFuncionario, PageRequest pageRequest);

    List<Conta> findByFuncionarioIdFuncionarioAndPix(Long idFuncionario, Pix pix, PageRequest pageRequest);

}
