package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Usuario;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>, JpaRepository<Usuario, Long>{


    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByEmail(String email);
}
