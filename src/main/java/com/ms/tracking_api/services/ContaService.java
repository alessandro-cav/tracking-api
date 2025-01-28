package com.ms.tracking_api.services;

import com.ms.tracking_api.dtos.requests.ContaRequest;
import com.ms.tracking_api.dtos.responses.ContaResponse;
import com.ms.tracking_api.entities.Conta;
import com.ms.tracking_api.entities.Usuario;
import com.ms.tracking_api.enuns.TipoChave;
import com.ms.tracking_api.enuns.TipoConta;
import com.ms.tracking_api.handlers.BadRequestException;
import com.ms.tracking_api.handlers.ObjetoNotFoundException;
import com.ms.tracking_api.repositories.ContaRepository;
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
public class ContaService {

    private final UsuarioService usuarioService;

    private final ContaRepository contaRepository;

    private final ModelMapper modelMapper;


    @Transactional
    public ContaResponse salvarContaDoUsuario(Long idUsuario, ContaRequest request) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        TipoChave tipoChave = TipoChave.buscarChavePix(request.getChavePix());
        TipoConta tipoConta = TipoConta.buscarTipoConta(request.getTipoConta());
        Conta conta = this.modelMapper.map(request, Conta.class);
        conta.setUsuario(usuario);
        conta.setTipoChave(tipoChave);
        conta.setTipoConta(tipoConta);
        conta = this.contaRepository.save(conta);
        return this.modelMapper.map(conta, ContaResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarTodasContaDoUsuario(Long idUsuario, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        List<Conta> contas = this.contaRepository.findAllByUsuarioIdUsuario(usuario.getIdUsuario(), pageRequest);
        return contas.stream().map(conta -> {
            return this.modelMapper.map(conta, ContaResponse.class);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarContaDoUsuarioPeloId(Long idUsuario, Long idConta) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        Conta conta =  this.contaRepository.findByUsuarioIdUsuarioAndIdConta(usuario.getIdUsuario(), idConta)
                .orElseThrow(() -> new ObjetoNotFoundException("Conta não encontrada!"));
        return this.modelMapper.map(conta, ContaResponse.class);
    }

    @Transactional
    public void deleteContaDoUsuario(Long idUsuario, Long idConta) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        this.contaRepository.findByUsuarioIdUsuarioAndIdConta(usuario.getIdUsuario(), idConta).ifPresentOrElse(conta -> {
            try {
                this.contaRepository.delete(conta);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Conta não pode ser excluida, pois está vinculada em algum funcionario");
            }
        }, () -> {
            throw new ObjetoNotFoundException("Conta não encontrado!");
        });
    }

    @Transactional
    public ContaResponse atualizarContaDoUsuario(Long idFuncionarios, Long idConta, ContaRequest request) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idFuncionarios);
        TipoChave tipoChave = TipoChave.buscarChavePix(request.getTipoChave());
        TipoConta tipoConta = TipoConta.buscarTipoConta(request.getTipoConta());

        return  this.contaRepository.findByUsuarioIdUsuarioAndIdConta(usuario.getIdUsuario(), idConta).map(conta -> {
            request.setIdConta(conta.getIdConta());
            conta = this.modelMapper.map(request, Conta.class);
            conta.setUsuario(usuario);
            conta.setTipoChave(tipoChave);
            conta.setTipoConta(tipoConta);
            conta = this.contaRepository.save(conta);
            return this.modelMapper.map(conta, ContaResponse.class);
        }).orElseThrow(() -> new ObjetoNotFoundException("Conta não encontrada!"));
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> buscarContaDoUsuarioPorTipoChave(Long idUsuario, String tipoPix, PageRequest pageRequest) {
        Usuario usuario = this.usuarioService.buscarUsuarioPeloId(idUsuario);
        TipoChave tipoChave = TipoChave.buscarChavePix(tipoPix);
        return this.contaRepository.findByUsuarioIdUsuarioAndTipoChave(usuario.getIdUsuario(), tipoChave, pageRequest).stream()
                .map(conta -> this.modelMapper.map(conta, ContaResponse.class))
                .collect(Collectors.toList());
    }
}
