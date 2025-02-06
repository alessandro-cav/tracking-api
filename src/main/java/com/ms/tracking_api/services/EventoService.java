package com.ms.tracking_api.services;


import com.ms.tracking_api.configs.validations.Validator;
import com.ms.tracking_api.dtos.requests.EventoRequest;
import com.ms.tracking_api.dtos.responses.EmpresaResponse;
import com.ms.tracking_api.dtos.responses.EventoResponse;
import com.ms.tracking_api.dtos.responses.EventoVagaResponse;
import com.ms.tracking_api.entities.*;
import com.ms.tracking_api.enuns.Status;
import com.ms.tracking_api.enuns.StatusEvento;
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
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventoService {

    private final ModelMapper modelMapper;

    private final EventoRepository repository;

    private final EmpresaService empresaService;

    private final Validator validator;

    @Transactional
    public EventoResponse salvar(EventoRequest eventoRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Empresa empresa = this.empresaService.buscarEmpresaPeloId(eventoRequest.getIdEmpresa());
        Evento evento = this.modelMapper.map(eventoRequest, Evento.class);
        evento.setEmpresa(empresa);
        evento.setEndereco(gerarEndereco(evento, eventoRequest));
        evento.setCnpjBanco(user.getCnpjBanco());
        evento.setStatusEvento(StatusEvento.ABERTO);
        evento = this.repository.save(evento);
        return gerarEnderecoResponse(evento);
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> buscarTodos(PageRequest pageRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBanco(user.getCnpjBanco(), pageRequest).stream()
                .map(evento -> gerarEnderecoResponse(evento))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> buscarNovoEventos(PageRequest pageRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Evento> eventos = this.repository.findByCnpjBanco(user.getCnpjBanco(), pageRequest);
        LocalDate agora = LocalDate.now();

        List<Evento> novosEventos = eventos.stream()
                .filter(evento -> !evento.getData().isBefore(agora))
                .collect(Collectors.toList());

        return novosEventos.stream()
                .map(this::gerarEnderecoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventoResponse buscarPeloId(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBancoAndIdEvento(user.getCnpjBanco(),id).map(evento -> {
            return gerarEnderecoResponse(evento);
        }).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }

    @Transactional
    public void delete(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.repository.findByCnpjBancoAndIdEvento(user.getCnpjBanco(),id).ifPresentOrElse(evento -> {
            try {
                this.repository.delete(evento);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Evento não pode ser exluido, pois está vinculada em alguma vaga ");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Evento não encontrado!");
        });
    }

    @Transactional
    public EventoResponse atualizar(Long id, EventoRequest eventoRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Empresa empresa = this.empresaService.buscarEmpresaPeloId(eventoRequest.getIdEmpresa());
        return this.repository.findByCnpjBancoAndIdEvento(user.getCnpjBanco(),id).map(evento -> {
            this.atualizarEvento(evento, eventoRequest);
            evento.setEmpresa(empresa);
            evento.setEndereco(gerarEndereco(evento, eventoRequest));
            evento.setCnpjBanco(user.getCnpjBanco());
            evento = this.repository.save(evento);
             return  gerarEnderecoResponse(evento);
        }).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBancpAndNomeContainingIgnoreCase(user.getCnpjBanco(), nome, pageRequest).stream()
                .map(evento -> gerarEnderecoResponse(evento))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Evento buscarEventoPeloId(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.repository.findByCnpjBancoAndIdEvento(user.getCnpjBanco(),id).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
    }

    @Transactional
    public void fecharEvento(Long idEvento) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Evento evento = this.repository.findByCnpjBancoAndIdEvento(user.getCnpjBanco(),idEvento).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado!"));
        if (evento.getStatusEvento() == StatusEvento.FECHADO) {
            throw new BadRequestException("O evento já está com o status FECHADO.");
        }
       evento.setStatusEvento(StatusEvento.FECHADO);
       this.repository.save(evento);
    }

    public List<EventoVagaResponse> buscarVagasPeloIdEvento(Long idEvento) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Vaga> vagas = this.repository.findVagasByCnpjBancoAndIdEvento(user.getCnpjBanco(),idEvento);
        if (vagas.isEmpty()) {
            throw new BadRequestException("Nenhuma vaga cadastrada para o evento especificado.");
        }
        return vagas.stream()
                .map(vaga -> modelMapper.map(vaga, EventoVagaResponse.class))
                .collect(Collectors.toList());
    }

    private EventoResponse gerarEnderecoResponse(Evento evento) {
        EventoResponse eventoResponse = this.modelMapper.map(evento, EventoResponse.class);
        eventoResponse.setLogradouro(evento.getEndereco().getLogradouro());
        eventoResponse.setNumero(evento.getEndereco().getNumero());
        eventoResponse.setEstado(evento.getEndereco().getEstado());
        eventoResponse.setCidade(evento.getEndereco().getCidade());
        eventoResponse.setBairro(evento.getEndereco().getBairro());
        eventoResponse.setCep(evento.getEndereco().getCep());
        EmpresaResponse empresaResponse = this.modelMapper.map(evento.getEmpresa(), EmpresaResponse.class);
        empresaResponse.setLogradouro(evento.getEmpresa().getEndereco().getLogradouro());
        empresaResponse.setNumero(evento.getEmpresa().getEndereco().getNumero());
        empresaResponse.setEstado(evento.getEmpresa().getEndereco().getEstado());
        empresaResponse.setCidade(evento.getEmpresa().getEndereco().getCidade());
        empresaResponse.setBairro(evento.getEmpresa().getEndereco().getBairro());
        empresaResponse.setCep(evento.getEmpresa().getEndereco().getCep());
        eventoResponse.setEmpresa(empresaResponse);
        return eventoResponse;
    }

    private Endereco gerarEndereco(Evento evento, EventoRequest eventoRequest) {
        Endereco endereco = evento.getEndereco() != null ? evento.getEndereco() : new Endereco();
        endereco.setLogradouro(eventoRequest.getLogradouro() == null ? endereco.getLogradouro() : eventoRequest.getLogradouro());
        endereco.setNumero(eventoRequest.getNumero() == null ? endereco.getNumero() : eventoRequest.getNumero());
        endereco.setEstado(eventoRequest.getEstado() == null ? endereco.getEstado() : eventoRequest.getEstado());
        endereco.setCidade(eventoRequest.getCidade() == null ? endereco.getCidade() : eventoRequest.getCidade());
        endereco.setBairro(eventoRequest.getBairro() == null ? endereco.getBairro() : eventoRequest.getBairro());
        endereco.setCep(eventoRequest.getCep() == null ? endereco.getCep() : eventoRequest.getCep());
        return endereco;
    }

    private Evento atualizarEvento(Evento evento, EventoRequest eventoRequest) {
        evento.setNome(eventoRequest.getNome() == null ? evento.getNome() : eventoRequest.getNome());
        evento.setHoraInicio(eventoRequest.getHoraInicio() == null ? evento.getHoraInicio() : eventoRequest.getHoraInicio());
        evento.setHoraFim(eventoRequest.getHoraFim() == null ? evento.getHoraFim() : eventoRequest.getHoraFim());
        evento.setData(eventoRequest.getData() == null ? evento.getData() : eventoRequest.getData());
        evento.setDetalhes(eventoRequest.getDetalhes() == null ? evento.getDetalhes() : eventoRequest.getDetalhes());
        evento.setStatusEvento(StatusEvento.ABERTO);
        return evento;
    }
}

