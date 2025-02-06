package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.entities.Usuario;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCnpjBancoAndEmail(String cnpjBanco, String email);

    Page<User> findByCnpjBanco(Example<User> example, PageRequest pageRequest, String cnpjBanco);

    List<User> findByCnpjBanco(String cnpjBanco, PageRequest pageRequest);

    Optional<User>  findByCnpjBancoAndId(String cnpjBanco, Long id);
}
