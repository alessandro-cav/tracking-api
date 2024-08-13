package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.EnderecoRequest;
import com.ms.tracking_api.dtos.responses.EnderecoResponse;
import com.ms.tracking_api.entities.Endereco;
import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Funcionario;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.EnderecoRepository;
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
public class EnderecoService {

    private final EventoService eventoService;

    private final FuncionarioService funcionarioService;

    private final EnderecoRepository enderecoRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public EnderecoResponse salvarEnderecoDoEvento(Long idEventos, EnderecoRequest request) {
        Evento evento = this.eventoService.buscarEventoPeloId(idEventos);
        Endereco endereco = this.modelMapper.map(request, Endereco.class);
        endereco.setEvento(evento);
        endereco = this.enderecoRepository.save(endereco);
        return this.modelMapper.map(endereco, EnderecoResponse.class);
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponse> buscarTodosEnderecoDoEvento(Long idEventos, PageRequest pageRequest) {
        Evento evento = this.eventoService.buscarEventoPeloId(idEventos);
        List<Endereco> enderecos = this.enderecoRepository.findAllByEventoIdEvento(evento.getIdEvento(), pageRequest);
        return enderecos.stream().map(endereco -> {
            return this.modelMapper.map(endereco, EnderecoResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnderecoResponse buscarEnderecoDoEventoPeloId(Long idEventos, Long idEndereco) {
        Evento evento = this.eventoService.buscarEventoPeloId(idEventos);
        Endereco endereco =  this.enderecoRepository.findByEventoIdEventoAndIdEndereco(evento.getIdEvento(), idEndereco)
                .orElseThrow(() -> new ObjetoNotFoundException("Endereço não encontrado!"));
        return this.modelMapper.map(endereco, EnderecoResponse.class);
    }

    @Transactional
    public void deleteEnderecoDoEvento(Long idEventos, Long idEndereco) {
        Evento evento = this.eventoService.buscarEventoPeloId(idEventos);
        this.enderecoRepository.findByEventoIdEventoAndIdEndereco(evento.getIdEvento(), idEndereco).ifPresentOrElse(endereco -> {
            try {
                this.enderecoRepository.delete(endereco);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Endereco não pode ser excluida, pois está vinculada em algum evento");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Endereço não encontrado!");
        });
    }

    @Transactional
    public EnderecoResponse atualizarEnderecoDoEvento(Long idEventos, Long idEndereco, EnderecoRequest request) {
        Evento evento = this.eventoService.buscarEventoPeloId(idEventos);
        return  this.enderecoRepository.findByEventoIdEventoAndIdEndereco(evento.getIdEvento(), idEndereco).map(endereco -> {
            request.setIdEndereco(endereco.getIdEndereco());
            endereco = this.modelMapper.map(request, Endereco.class);
            endereco.setEvento(evento);
            endereco = this.enderecoRepository.save(endereco);
            return this.modelMapper.map(endereco, EnderecoResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Endereço não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponse> buscarEnderecoDoEventoPorNome(Long idEventos, String logradouro, PageRequest pageRequest) {
        Evento evento = this.eventoService.buscarEventoPeloId(idEventos);
        return this.enderecoRepository.findByEventoIdEventoAndLogradouroContainingIgnoreCase(evento.getIdEvento(), logradouro, pageRequest).stream()
                .map(endereco -> this.modelMapper.map(endereco, EnderecoResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public EnderecoResponse salvarEnderecoDoFuncionario(Long idFuncionarios, EnderecoRequest request) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Endereco endereco = this.modelMapper.map(request, Endereco.class);
        endereco.setFuncionario(funcionario);
        endereco = this.enderecoRepository.save(endereco);
        return this.modelMapper.map(endereco, EnderecoResponse.class);
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponse> buscarTodosEnderecoDoFuncionario(Long idFuncionarios, PageRequest pageRequest) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        List<Endereco> enderecos = this.enderecoRepository.findAllByFuncionarioIdFuncionario(funcionario.getIdFuncionario(), pageRequest);
        return enderecos.stream().map(endereco -> {
            return this.modelMapper.map(endereco, EnderecoResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnderecoResponse buscarEnderecoDoFuncionarioPeloId(Long idFuncionarios, Long idEndereco) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        Endereco endereco =  this.enderecoRepository.findByFuncionarioIdFuncionarioAndIdEndereco(funcionario.getIdFuncionario(), idEndereco)
                .orElseThrow(() -> new ObjetoNotFoundException("Endereço não encontrado!"));
        return this.modelMapper.map(endereco, EnderecoResponse.class);
    }

    @Transactional
    public void deleteEnderecoDoFuncionario(Long idFuncionarios, Long idEndereco) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        this.enderecoRepository.findByFuncionarioIdFuncionarioAndIdEndereco(funcionario.getIdFuncionario(), idEndereco).ifPresentOrElse(endereco -> {
            try {
                this.enderecoRepository.delete(endereco);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Endereco não pode ser excluida, pois está vinculada em algum funcionario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Endereço não encontrado!");
        });
    }

    @Transactional
    public EnderecoResponse atualizarEnderecoDoFuncionario(Long idFuncionarios, Long idEndereco, EnderecoRequest request) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        return  this.enderecoRepository.findByFuncionarioIdFuncionarioAndIdEndereco(funcionario.getIdFuncionario(), idEndereco).map(endereco -> {
            request.setIdEndereco(endereco.getIdEndereco());
            endereco = this.modelMapper.map(request, Endereco.class);
            endereco.setFuncionario(funcionario);
            endereco = this.enderecoRepository.save(endereco);
            return this.modelMapper.map(endereco, EnderecoResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Endereço não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponse> buscarEnderecoDoFuncionarioPorNome(Long idFuncionarios, String logradouro, PageRequest pageRequest) {
        Funcionario funcionario = this.funcionarioService.buscarFuncionarioPeloId(idFuncionarios);
        return this.enderecoRepository.findByFuncionarioIdFuncionarioAndLogradouroContainingIgnoreCase(funcionario.getIdFuncionario(), logradouro, pageRequest).stream()
                .map(endereco -> this.modelMapper.map(endereco, EnderecoResponse.class))
                .collect(Collectors.toList());
    }
}
