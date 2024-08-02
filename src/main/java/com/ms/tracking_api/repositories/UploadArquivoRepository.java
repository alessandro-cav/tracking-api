package com.ms.tracking_api.repositories;


import com.ms.tracking_api.entities.UploadArquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UploadArquivoRepository extends PagingAndSortingRepository<UploadArquivo, Long>, JpaRepository<UploadArquivo, Long> {
}