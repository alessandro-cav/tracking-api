package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Conta;
import com.ms.tracking_api.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface EnderecoRepository extends PagingAndSortingRepository<Endereco, Long>, JpaRepository<Endereco, Long>{

}
