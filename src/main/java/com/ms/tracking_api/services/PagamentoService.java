package com.ms.tracking_api.services;

import com.ms.tracking_api.repositories.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;
}
