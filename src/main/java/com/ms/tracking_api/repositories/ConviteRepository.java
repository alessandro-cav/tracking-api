package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Convite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConviteRepository extends PagingAndSortingRepository<Convite, Long>, JpaRepository<Convite, Long> {

    Optional<Convite> findByEmail(String email);

    Optional<Convite> findByEmailAndCodigo(String email, String codigo);
}
