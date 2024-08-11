package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.EventoRequest;
import com.ms.tracking_api.dtos.responses.EmpresaResponse;
import com.ms.tracking_api.dtos.responses.EventoResponse;
import com.ms.tracking_api.entities.Empresa;
import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.User;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final ModelMapper modelMapper;

    private final EventoRepository repository;

    private final EmpresaService empresaService;

    private final Validator validator;

    public EventoResponse salvar(EventoRequest eventoRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate data = LocalDate.parse(eventoRequest.getData(), formatter);

        Empresa empresa =  this.empresaService.buscarEmpresaPeloId(eventoRequest.getIdEmpresa());
        Evento evento  = this.modelMapper.map(eventoRequest, Evento.class);
        evento.setEmpresa(empresa);
        evento.setData(data);
        evento = this.repository.save(evento);
        return this.modelMapper.map(evento, EventoResponse.class);
    }

    public List<EventoResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(evento -> this.modelMapper.map(evento, EventoResponse.class))
                .collect(Collectors.toList());
    }

    public List<EventoResponse> buscarNovoEventos(PageRequest pageRequest) {
        List<Evento> eventos = this.repository.findAll();
        LocalDate agora = LocalDate.now();
        List<Evento> novosEventos = eventos.stream()
                .filter(evento -> !evento.getData().isBefore(agora))
                .collect(Collectors.toList());
        return novosEventos.stream()
                .map(evento -> this.modelMapper.map(evento, EventoResponse.class))
                .collect(Collectors.toList());
    }

    public EventoResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(evento -> {
            return this.modelMapper.map(evento, EventoResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }

    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(evento -> {
            try{
                this.repository.delete(evento);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Evento não pode ser exluido, pois está vinculada em alguma vaga ");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Evento não encontrado!");
        });
    }

    public EventoResponse atualizar(Long id, EventoRequest eventoRequest) {
        Empresa empresa =  this.empresaService.buscarEmpresaPeloId(eventoRequest.getIdEmpresa());
        return this.repository.findById(id).map(evento -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate data = LocalDate.parse(eventoRequest.getData(), formatter);

            eventoRequest.setIdEvento(evento.getIdEvento());
            evento  = this.modelMapper.map(eventoRequest, Evento.class);
            evento.setEmpresa(empresa);
            evento.setData(data);
            evento = this.repository.save(evento);
            return this.modelMapper.map(evento, EventoResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }

    public List<EventoResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByNomeContainingIgnoreCase(nome, pageRequest).stream()
                .map(evento -> this.modelMapper.map(evento, EventoResponse.class))
                .collect(Collectors.toList());
    }
    public Evento buscarEventoPeloId(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }


    public Evento teste(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }
}

