package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.EventoEndereco;
import com.ms.tracking_api.entities.FuncionarioEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface FuncionarioEnderecoRepository extends PagingAndSortingRepository<FuncionarioEndereco, Long>, JpaRepository<FuncionarioEndereco, Long>{

}
