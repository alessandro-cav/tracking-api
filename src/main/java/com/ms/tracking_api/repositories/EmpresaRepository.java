package com.ms.tracking_api.repositories;

import com.ms.tracking_api.entities.Empresa;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends PagingAndSortingRepository<Empresa, Long>, JpaRepository<Empresa, Long>{

    Optional<Empresa> findByCnpjBancoAndCnpj(String cnpj, String empresaRequestCnpj);

    List<Empresa> findByCnpjBanco(String cnpjBanco, PageRequest pageRequest);

    Optional<Empresa> findByCnpjAndEmail(String cnpjBanco, String email);

    List<Empresa> findByCnpjBancoAndNomeContainingIgnoreCase(String cnpjBanco, String nome, PageRequest pageRequest);

    Optional<Empresa> findByCnpjAndIdEmpresa(String cnpjBanco, Long idEmpresa);
}
