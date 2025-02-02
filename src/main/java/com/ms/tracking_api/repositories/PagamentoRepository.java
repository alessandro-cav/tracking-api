package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends PagingAndSortingRepository<Pagamento, Long>, JpaRepository<Pagamento, Long> {
}
