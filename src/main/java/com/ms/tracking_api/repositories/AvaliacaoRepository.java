package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long>, JpaRepository<Avaliacao, Long> {


}
