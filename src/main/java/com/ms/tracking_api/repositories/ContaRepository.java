package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ContaRepository extends PagingAndSortingRepository<Conta, Long>, JpaRepository<Conta, Long>{

}
