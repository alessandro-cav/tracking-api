package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.EventoEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface EventoEnderecoRepository extends PagingAndSortingRepository<EventoEndereco, Long>, JpaRepository<EventoEndereco, Long>{

}
