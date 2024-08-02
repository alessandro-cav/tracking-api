package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.VagaRequest;
import com.ms.tracking_api.dtos.responses.VagaResponse;
import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Vaga;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class VagaService {

    private final ModelMapper modelMapper;

    private final VagaRepository repository;

    private final EventoService eventoService;

    public VagaResponse salvar(VagaRequest vagaRequest) {
        Evento evento =  this.eventoService.buscarEventoPeloId(vagaRequest.getIdEvento());
        Vaga vaga  = this.modelMapper.map(vagaRequest, Vaga.class);
        vaga.setEvento(evento);
        vaga = this.repository.save(vaga);
        return this.modelMapper.map(vaga, VagaResponse.class);
    }

    public List<VagaResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(vaga -> this.modelMapper.map(vaga, VagaResponse.class))
                .collect(Collectors.toList());
    }

    public VagaResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(vaga -> {
            return this.modelMapper.map(vaga, VagaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Vaga não encontrada!"));
    }

    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(vaga -> {
            try{
                this.repository.delete(vaga);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Vaga não pode ser exluido, pois está vinculada em algum funcionario ");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Vaga não encontrada!");
        });
    }

    public VagaResponse atualizar(Long id, VagaRequest vagaRequest) {
        Evento evento =  this.eventoService.buscarEventoPeloId(vagaRequest.getIdEvento());
        return this.repository.findById(id).map(vaga -> {
            vagaRequest.setIdVaga(vaga.getIdVaga());
            vaga  = this.modelMapper.map(vagaRequest, Vaga.class);
            vaga.setEvento(evento);
            vaga = this.repository.save(vaga);
            return this.modelMapper.map(vaga, VagaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Vaga não encontrada!"));
    }

    public List<VagaResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByVagaContainingIgnoreCase(nome, pageRequest).stream()
                .map(vaga -> this.modelMapper.map(vaga, VagaResponse.class))
                .collect(Collectors.toList());
    }
    public Vaga buscarVagaPeloId(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Vaga não encontrada!"));
    }

}
