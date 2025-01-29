package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.StatusVagaFechadaRequest;
import com.ms.tracking_api.dtos.requests.VagaRequest;
import com.ms.tracking_api.dtos.responses.EmpresaResponse;
import com.ms.tracking_api.dtos.responses.EventoResponse;
import com.ms.tracking_api.dtos.responses.VagaResponse;
import com.ms.tracking_api.entities.Evento;
import com.ms.tracking_api.entities.Vaga;
import com.ms.tracking_api.enuns.StatusVaga;
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
public class VagaService {

    private final ModelMapper modelMapper;

    private final VagaRepository repository;

    private final EventoService eventoService;

    @Transactional
    public VagaResponse salvar(VagaRequest vagaRequest) {
        Evento evento = this.eventoService.buscarEventoPeloId(vagaRequest.getIdEvento());
        Vaga vaga = this.modelMapper.map(vagaRequest, Vaga.class);
        vaga.setEvento(evento);
        vaga.setStatusVaga(StatusVaga.ABERTA);
        vaga = this.repository.save(vaga);
        return gerarEnderecoResponse(vaga);
    }

    @Transactional(readOnly = true)
    public List<VagaResponse> buscarTodos(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest).stream()
                .map(vaga -> gerarEnderecoResponse(vaga))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VagaResponse buscarPeloId(Long id) {
        return this.repository.findById(id).map(vaga -> {
            return gerarEnderecoResponse(vaga);
        }).orElseThrow(() -> new ObjetoNotFoundException("Vaga não encontrada!"));
    }

    @Transactional
    public void delete(Long id) {
        this.repository.findById(id).ifPresentOrElse(vaga -> {
            try {
                this.repository.delete(vaga);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Vaga não pode ser exluido, pois está vinculada em algum funcionario ");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Vaga não encontrada!");
        });
    }

    @Transactional
    public VagaResponse atualizar(Long id, VagaRequest vagaRequest) {
        Evento evento = this.eventoService.buscarEventoPeloId(vagaRequest.getIdEvento());
        return this.repository.findById(id).map(vaga -> {
            vagaRequest.setIdVaga(vaga.getIdVaga());
            vaga = this.modelMapper.map(vagaRequest, Vaga.class);
            vaga.setEvento(evento);
            vaga = this.repository.save(vaga);
            return gerarEnderecoResponse(vaga);
        }).orElseThrow(() -> new ObjetoNotFoundException("Vaga não encontrada!"));
    }

    @Transactional(readOnly = true)
    public List<VagaResponse> buscarPorNome(String nome, PageRequest pageRequest) {
        return this.repository.findByDescricaoVagaContainingIgnoreCase(nome, pageRequest).stream()
                .map(vaga -> gerarEnderecoResponse(vaga))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Vaga buscarVagaPeloId(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Vaga não encontrada!"));
    }

    @Transactional
    public void mudarsStatusParaFechada(StatusVagaFechadaRequest request) {
        Vaga vaga = this.repository.findById(request.getIdVaga()).get();

        if (vaga.getStatusVaga() == StatusVaga.FECHADA) {
            throw new BadRequestException("A vaga já está fechada.");
        }
        vaga.setStatusVaga(StatusVaga.FECHADA);
        this.repository.save(vaga);
    }

    private VagaResponse gerarEnderecoResponse(Vaga vaga) {
       VagaResponse vagaResponse =  this.modelMapper.map(vaga, VagaResponse.class);
        EventoResponse eventoResponse = this.modelMapper.map(vaga.getEvento(), EventoResponse.class);
        eventoResponse.setLogradouro(vaga.getEvento().getEndereco().getLogradouro());
        eventoResponse.setNumero(vaga.getEvento().getEndereco().getNumero());
        eventoResponse.setEstado(vaga.getEvento().getEndereco().getEstado());
        eventoResponse.setCidade(vaga.getEvento().getEndereco().getCidade());
        eventoResponse.setBairro(vaga.getEvento().getEndereco().getBairro());
        eventoResponse.setCep(vaga.getEvento().getEndereco().getCep());
        EmpresaResponse empresaResponse = this.modelMapper.map(vaga.getEvento().getEmpresa(), EmpresaResponse.class);
        empresaResponse.setLogradouro(vaga.getEvento().getEmpresa().getEndereco().getLogradouro());
        empresaResponse.setNumero(vaga.getEvento().getEmpresa().getEndereco().getNumero());
        empresaResponse.setEstado(vaga.getEvento().getEmpresa().getEndereco().getEstado());
        empresaResponse.setCidade(vaga.getEvento().getEmpresa().getEndereco().getCidade());
        empresaResponse.setBairro(vaga.getEvento().getEmpresa().getEndereco().getBairro());
        empresaResponse.setCep(vaga.getEvento().getEmpresa().getEndereco().getCep());
        eventoResponse.setEmpresa(empresaResponse);
        vagaResponse.setEvento(eventoResponse);
        return vagaResponse;
    }
}
