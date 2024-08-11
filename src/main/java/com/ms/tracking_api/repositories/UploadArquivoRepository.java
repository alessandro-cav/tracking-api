package com.ms.tracking_api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UploadArquivoRepository extends PagingAndSortingRepository<UploadArquivo, Long>, JpaRepository<UploadArquivo, Long> {
}